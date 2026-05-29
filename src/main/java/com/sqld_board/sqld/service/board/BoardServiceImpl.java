package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.dto.request.board.BoardRequest;
import com.sqld_board.sqld.dto.request.board.CommentRequest;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.dto.response.code.CategoryResponse;
import com.sqld_board.sqld.exception.board.*;
import com.sqld_board.sqld.exception.common.MemberNotFoundException;
import com.sqld_board.sqld.mapper.*;
import com.sqld_board.sqld.model.board.Board;
import com.sqld_board.sqld.model.board.BoardFile;
import com.sqld_board.sqld.model.commentManagement.CommentManagement;
import com.sqld_board.sqld.model.member.MemberInfo;
import com.sqld_board.sqld.service.notificationStomp.NotificationStompService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardMapper   boardMapper;
    private final CommentMapper commentMapper;
    private final MemberMapper   memberMapper;
    private final BoardMasterMapper boardMasterMapper;
    private final CommonCodeDetailMapper commonCodeDetailMapper;

    private final ViewCountRedisService    viewCountRedisService;
    private final NotificationStompService notificationStompService;


    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryListByBoardCode(String boardCode) {
        // 1. boardCode 유무 확인
        if(boardCode == null || boardCode.isEmpty()){
            throw new NotFoundBoardCodeException();
        }

        // 2.카테고리 목록 조회.
        return commonCodeDetailMapper.getCategoryListByBoardCode(boardCode);

    }

    @Override
    public void deleteMyScrapPage(List<Long> scrapIds, String memberId) {

        if(scrapIds == null || scrapIds.isEmpty()) {
            throw new MissingDeleteTargetException();
        }
        boardMapper.deleteMyScrapPage(scrapIds, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> searchScrapMyPage(int page, int size, String keyword, String memberId) {

        // 1. 마이너스 페이지 방지
        if(page < 1){
            page = 1;
        }
        int offset = (page - 1) * size;

        //2. 검색 조건에 맞는 리스트와 총 개수 조회
        List<Board> result =  boardMapper.searchScrapMyPage(offset, size, keyword, memberId);
        int totalCount = boardMapper.getSearchScrapTotalCount(keyword, memberId);

        //3. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("list", result.stream().map(BoardResponse::new).collect(Collectors.toList()));
        response.put("totalCount", totalCount);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));

        return response;
    }

    @Override
    public Boolean insertBoardScrap(Long boardId, String memberId) {
        // 스크랩 여부 확인
        int count = boardMapper.checkBoardScrap(boardId, memberId);
        log.info("count :---------------- " + count);

        if(count >  0){
            // 페이지 스크랩이 있는 경우, 스크랩 삭제 (단건)
         boardMapper.deleteMyScrapSinglePage(boardId, memberId);
         log.info("삭제 완료");
         return false;
         }else{
        // 페이지 스크랩이 없는 경우, 스크랩 추가 (단건)
        boardMapper.insertBoardScrap(boardId, memberId);
        log.info("추가 완료");
        return true;
        }
    }

    @Override
    public void deleteFiles(List<Long> fileIds) {
        if(fileIds == null || fileIds.isEmpty()) {
            return;
        }

        // 삭제할 파일들의 정보를 DB에서 가져옴(실제경로 파악)
        List<BoardFile> filesToDelete = boardMapper.getBoardFileInfoByIds(fileIds);

        for(BoardFile file : filesToDelete){
            File physicalFile = new File(file.getFilePath());
            if(physicalFile.exists()){
                physicalFile.delete(); //파일 삭제
            }
        }
        // DB에서 파일 정보 일괄 삭제
        boardMapper.deleteBoardFiles(fileIds);
    }
    //  1. 순서 주의: DB에서 데이터를 먼저 DELETE 해버리면, 나중에 물리 파일을 지우려고 할 때 경로를 알 수 없게 됩니다. 반드시 정보 조회 -> 물리 삭제 -> DB 삭제 순서를 지켜주세요.
    //   2. 파일 경로 체크: new File(path)를 생성할 때 경로가 정확한지 확인하세요. (우리는 이미 FILE_PATH에 전체 경로를 저장하고 있으므로 그대로 쓰시면 됩니다.)
    //   3. 예외 처리: 만약 물리 파일 삭제에 실패하더라도(exists()가 false 등) DB 데이터는 지워지도록 로직을 짜는 것이 일반적입니다. (파일이 이미 없을 수도 있으니까요.)

    @Override
    public BoardFile getBoardFileInfo(Long fileId) {

        BoardFile fileInfo = boardMapper.getBoardFileInfo(fileId);
                if(fileInfo == null){
                    throw new NotBoardContentException();
                }
                return fileInfo;
    }

    @Override
    public Resource downloadFile(Long fileId) {
        // DB에서 파일정보 조회
         BoardFile fileInfo = boardMapper.getBoardFileInfo(fileId);
         if(fileInfo == null) { // 파일이 없는 경우, 예외처리
             throw new NotFoundFileIdException(); // 파일정보를 찿을 수 없습니다.
         }
        try {
            // 파일 경로 파악 및 Resource 객체 생성
            Path path = Paths.get(fileInfo.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            // 실제 파일이 존재하는지 확인
            if(resource.exists() && resource.isReadable()){
                return resource;
            }else{
                // 파일 정보는 DB에 있는데, 실제 하드에 파일이 없는경우
                throw new NotFoundFileIdException();
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException();// 파일 다운로드중 오류가 발생했습니다.
        }

    }

    @Override
    @Cacheable(value = "popularPosts", key = "'top5'") // DB 조회 부담 감소를 위한, Caffeine 캐시 사용.
    public List<BoardResponse> getPopularBoards() {
        log.info("인기 게시물 조회를 위해 DB에 접근한다. (캐시 미적용 시에만 출력)");

        //1. 최근 15일 인기 게시글 조회
        List<Board> boards = boardMapper.getPopularBoards();

        //2. 결과가 없는 경우,
        if(boards == null || boards.isEmpty()){
            log.info("최근 15일간 인기 게시물이 없어 전체 최신글로 대체한다.");

            // 기간 제한 없이 추천수.조회수 순으로 5개만 가져오는 별도 쿼리 호출 가능
            boards = boardMapper.getRecentFallbackBoards();
        }
        //[추가] 리스트의 각 항목에 Redis 조회수를 합산
        if(boards !=null && !boards.isEmpty()){
            for(Board board : boards){
                // 각 게시글 ID에 해당하는 redis 조회수 가져오기
                int redisViewCount= viewCountRedisService.getViewCount(board.getBoardId());

                // DB 조회수에 Redis를 조회수를 더한다.
                board.combineRedisViewCount(redisViewCount);
            }
        }
        return boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 댓글을 작성하고 저장한다.
     * @param request 댓글 정보
     * @param memberId 인증된 작성자 ID
     * @return
     */
    @Override
    @Transactional
    public Long writeComment(CommentRequest request, String memberId) {

    // 1. 원글 정보 조회 (알림 대상 확인용)
    BoardResponse boardInfo = boardMapper.getSearchBoard(request.getBoardId(), null);
    if(boardInfo == null){
        throw new NotFoundContentBoardException();
    }

        // 전달받은 memberId를 통한 회원 정보 조회 (유효성 체크)
    MemberInfo memberInfo = memberMapper.readMemberByMemberId(memberId)
                                        .orElseThrow(MemberNotFoundException::new);

    // 댓글 객체 생성 ( 빌더 패턴 사용 시)
        CommentManagement commentManagement = CommentManagement.builder()
                .boardId(request.getBoardId())
                .memberId(memberInfo.getMemberId())    // 서버가 확인한 고유 Id 셋팅
                .userId(memberInfo.getUserId())
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .build();
    // DB 저장
        commentMapper.insertComment(commentManagement);

        Long commentId = commentManagement.getCommentId();

        if(commentId == null){
            throw new FailureCreateCommentException(); // 댓글 저장 중 오류 발생
        }

        // 2. [알림 트리거] 원글 작성자와 댓글 작성자가 다를 때만 알림 발송
        String targetId = null;
        String alertMessage = "";

        if(request.getParentCommentId() != null) {
            // [대댓글인 경우]
            CommentManagement parentCommentManagement = commentMapper.getComment(request.getParentCommentId());
            if(parentCommentManagement != null){
                targetId = parentCommentManagement.getMemberId();
                alertMessage = "내 댓글에 답글이 달렸습니다: ";
            }
        } else {
            // [일반 댓글인 경우]
            targetId = boardInfo.getMemberId();
            alertMessage = "새로운 댓글이 달렸습니다: ";
        }

        //최종 발송 조건 (대상이 존재하고, 내가 쓴 글 / 댓글이 아닐때
        if(targetId != null && !targetId.equals(memberId)){
            RealTimeMessage notifMsg =RealTimeMessage.builder()
                    .type(RealTimeMessage.MessageType.NOTIFY)                 // 알림 타입
                    .senderId(memberId)                                       // 댓글 작성자
                    .targetId(targetId)                                       // 원글 작성자 (알림 수신인)
                    .content(alertMessage + request.getContent())
                    .targetUrl("/board/view?boardId=" + request.getBoardId()) // 클릭 시 이동할 링크
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            notificationStompService.sendNotification(notifMsg); //DB 저장 +  실시간 발송 한 번에 종료.
        }
        return commentId;
    }

    /**
     * 게시판 댓글을 확인합니다.
     * @param boardId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentManagement> getCommentList(Long boardId) {
        if(boardId == null) {
            throw new NotBoardContentException();
        }
        return commentMapper.getCommentList(boardId);
    }

    /**
     * 댓글 내용을 수정합니다. (작성자 검증 포함)
     * @param commentId 댓글 ID
     * @param content 수정할 내용
     * @param memberId 수정 요청한 사용자 ID
     */
    @Override
    @Transactional
    public void updateComment(Long commentId, String content, String memberId) {

        CommentManagement foundCommentManagement = commentMapper.getComment(commentId);
        if (foundCommentManagement == null) throw new RuntimeException("댓글을 찾을 수 없습니다.");
        
        // 작성자 본인인지 확인 (기존 DB의 USER_ID 컬럼 대신 MEMBER_ID 컬럼 사용)
        if (!foundCommentManagement.getMemberId().equals(memberId)) {
            throw new RuntimeException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        commentMapper.updateComment(commentId, content);
    }


    /**
     * 댓글을 삭제 처리합니다. (작성자 검증 포함)
     * @param commentId 댓글 ID
     * @param memberId 삭제 요청한 사용자 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId, String memberId) {

        //1. 해당 댓글 정보조회
        CommentManagement currentCommentManagement = commentMapper.getComment(commentId);
        if (currentCommentManagement == null) {
            throw new NotBoardCommentException();
        }

        //  권한 확인 (본인 글 여부)
        if (!currentCommentManagement.getMemberId().equals(memberId)) {
            throw new NotPostOwnerException();
        }

        // 2. 자식 (대댓글)이 있는지 확인
        int childCount =commentMapper.getChildCommentCount(commentId);

        // 자식이 있는 경우 -> 내용만 마스킹 처리
        if(childCount > 0 ){
            commentMapper.maskCommentAsDeleted(commentId);
        } else{
        // 자식이 없는 경우, 완전 삭제
            commentMapper.deleteComment(commentId);

            // 3.만약 이제 대댓글 이었다면
            if(currentCommentManagement.getParentCommentId() != null){
                Long parentId = currentCommentManagement.getParentCommentId();
                CommentManagement parentCommentManagement = commentMapper.getComment(parentId);

                // 4. 부모가  '삭제된 댓글 상태이고, 이제 남은 자식이 하나도 없다면?
                if(parentCommentManagement != null && "삭제된 댓글입니다.".equals(parentCommentManagement.getContent())){
                    int remainingChildren = commentMapper.getChildCommentCount(parentId);
                    if(remainingChildren == 0){
                        // 부모 댓글도 이제 완전히 삭제 처리
                        commentMapper.deleteComment(parentId);
                    }
                 }
            }
        }
    }


    /**
     * 게시판에 새로운 글을 작성하고 저장합니다.
     *
     * @param request 게시글 요청 정보 (제목, 내용 등)
     * @return DB에 저장된 후 생성된 게시글 ID (BOARD_ID)
     */
    @Override
    @Transactional
    public Long writeBoard(BoardRequest request, String memberId) {

        // 1. 게시판 유무 확인
        validateBoardCodeExists(request.getBoardCode());

        // 3. 카테고리 유효성 검증
        isValidateCategoryForBoard(request.getBoardCode(), request.getCategoryId());

        // 2. 해당 접속 아이디와 글 작성하려고 한 아이디가 동일인지 확인
        MemberInfo memberInfo = memberMapper.readMemberByMemberId(memberId)
                                            .orElseThrow(MemberNotFoundException::new);

        // 2. Build
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                // 실제 고유 아이디 저장
                .memberId(memberId)
                // 기존 컬럼 유지를 위해 회원 정보에서 아이디를 꺼내 저장
                .userId(memberInfo.getUserId())
                .boardCode(request.getBoardCode())
                .categoryId(request.getCategoryId())
                .tagName(request.getTagName())
                .build();

            // MyBatis Mapper를 통해 DB에 저장 (생성된 ID는 board 객체에 자동으로 담김)
            boardMapper.insertBoard(board);

            if(board.getBoardId() == null){
                throw new FailureCreateContentException();
            }

            return board.getBoardId();
    }

    /**
     * 특정 게시글에 첨부된 여러 개의 파일을 저장하고 DB에 정보를 기록합니다.
     *
     * @param boardId 파일을 연결할 게시글의 ID
     * @param files   업로드된 파일 리스트
     */
    @Transactional
    @Override
    public void uploadFiles(Long boardId, List<MultipartFile> files) {

//        if (files == null || files.isEmpty()) {
//            return;
//        }

        try {
            // 저장 디렉토리가 없으면 생성
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile file : files) {
                String originame = file.getOriginalFilename();
                // 중복 방지를 위해 UUID와 원본 파일명을 조합하여 저장
                String saveNmee = UUID.randomUUID().toString() + "_" + originame;
                Path path = Paths.get(uploadPath, saveNmee);

                // 서버 폴더에 파일 물리 저장.
                file.transferTo(path);

                // 저장된 파일 정보를 DB 모델로 생성
                BoardFile boardFile = BoardFile.builder()
                        .boardId(boardId)
                        .originName(originame)
                        .saveName(saveNmee)
                        .filePath(path.toString())
                        .fileSize(file.getSize())
                        .build();

                // 파일 정보 DB 저장
                boardMapper.insertBoardFile(boardFile);
            } // end of for

        } catch (IOException e) {
            log.error("File upload error", e);
            throw new FailureFileUploadErrorException(); // 파일 업로드 실패 예외 처리
        }
    }// End of uploadFiles()

    /**
     * CKEditor 5 에디터 전용 단일 이미지 업로드 처리입니다.
     * 에디터에서 전송한 이미지를 저장하고 접근 가능한 브라우저용 URL을 반환합니다.
     *
     * @param file 업로드된 이미지 파일
     * @return 브라우저에서 접근 가능한 URL (예: /uploads/파일명.png)
     * @throws IOException 파일 저장 중 발생하는 예외
     */
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new FileNotFoundException();
        }

        // 저장 디렉토리 확인 및 생성
        File direectory = new File(uploadPath);
        if(!direectory.exists()){
            direectory.mkdirs();
        }
        //파일명 중복 방지 처리
        String originName = file.getOriginalFilename();
        String saveName = UUID.randomUUID().toString() + "_" + originName;
        Path path = Paths.get(uploadPath, saveName);

        // 이미지 파일 서버에 물리 저장
        file.transferTo(path);

        // 브라우저 접근 가능한 URL 반환
        // "/" 슬러시 누락시 이미지를 찾을 수 없는 404에러가 발생할 수 있다.
        return "/uploads/" + saveName;
    }

    /**
     * 게시글 수정
     * @param boardId
     * @param request
     * @param memberId
     */
    @Override
    public void updateBoard(Long boardId, BoardRequest request, String memberId) {

        // 1. 게시판 유무 확인
        validateBoardCodeExists(request.getBoardCode());

        // 2. 게시글 정보 확인
        BoardResponse board = boardMapper.getSearchBoard(boardId, memberId);
        if(board == null) {
            throw new NotBoardContentException();
        }
        // 3. 카테고리 유효성 검증
        isValidateCategoryForBoard(request.getBoardCode(), request.getCategoryId());

        // DB에 저장된 작성자와 현재 요청한 사람이 같은지 비교
        if(!board.getMemberId().equals(memberId)){
            throw new NotPostOwnerException();
        }

        Board updateData = Board.builder()
                .boardId(boardId)
                .title(request.getTitle())
                .content(request.getContent())
                .boardCode(request.getBoardCode())
                .categoryId(request.getCategoryId())
                .tagName(request.getTagName())
                .build();

        int result = boardMapper.updateBoard(updateData);
                if(result == 0){
                    throw new FailureUpdateContentException();
                }
    }

    /**
     * ID를 기준으로 특정 게시글을 조회하며, 조회수도 함께 증가시킵니다.
     * @param boardId 조회할 게시글의 ID
     * @param memberId 선택적 사용자 고유 ID (추천 여부 확인용)
     */
    @Override
    @Transactional
    public BoardResponse getSearchBoard(Long boardId, String memberId) {
        // 1. 조회수 증가 (edis 에 저장)
        this.incrementViewCount(boardId);
//        boardMapper.incrementViewCount(boardId);

        // 2. 게시글 정보 조회
        BoardResponse response = boardMapper.getSearchBoard(boardId,memberId);

        // 게시글이 없는 경우 예외 발생
        if(response == null){
            throw new NotFoundContentBoardException(); // 파일을 찾을 수 업음.
        }

        //실시간 합산: DB 숫자(예: 100) + Redis에 대기중인 숫자 (예: 5) =105
        int redisViewCount = viewCountRedisService.getViewCount(boardId);
        response.setViewCount(response.getViewCount() + redisViewCount);


        // 3. 첨부 파일 리스트 조회 및 세팅
        List<BoardFile> files = boardMapper.getBoardFileList(boardId);
        response.setFileList(files);

        // 4. 추천 여부 확인 (사용자 ID가 있을 경우)
        if (memberId != null && !memberId.isEmpty()) {
            boolean isLiked = boardMapper.checkBoardLike(boardId, memberId) > 0;
            response.setLiked(isLiked);

        // [추가 스크랩 여부 확인]
            boolean isScrapped = boardMapper.checkBoardScrap(boardId, memberId) > 0;
            response.setScrapped(isScrapped);
        }
        return response;
    } // End Of getSearchBoard();

    /**
     * 전체 게시글 목록을 조회합니다.
     * @return
     */
    @Override
    @Transactional
    public List<BoardResponse> getBoardList() {
        List<Board> boards = boardMapper.getBoardList();

        // 리스트의 각 항목에 대해 Redis 조회수를 합산한다.
        if(boards != null && !boards.isEmpty()){
            for(Board board : boards){
                // 각 게시글 ID과 Redis 조회수를 가져온다.
                int redisViewCount = viewCountRedisService.getViewCount(board.getBoardId());

                //DB 조회수에 Redis 조회수를 더해준다.
                board.combineRedisViewCount(redisViewCount);
            }
        }

        return boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 로그인 시, 회원 정보 카드에서 내가 작성한 리스트 조회
     * @param page
     * @param size
     * @param boardCode
     * @param categoryId
     * @param tagName
     * @param memberId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> getBoardMyList(int page, int size, String boardCode
                                             ,String categoryId, String tagName
                                             ,String keyword, String memberId) {
       // 마이너스 페이지 방지
        if(page < 1){
            page = 1;
        }

        int offset = (page-1 ) * size; // offset는 최소 0이 보장

        List<Board> boards =  boardMapper.getBoardListWithPaging(offset, size, boardCode, categoryId, memberId, keyword, tagName);

        //[추가] 리스트의 각 항목에 Redis 조회수를 합산
        if(boards !=null && !boards.isEmpty()){
            for(Board board : boards){
                // 각 게시글 ID에 해당하는 Redis 조회수를 가져온다.
                int redisViewCount = viewCountRedisService.getViewCount(board.getBoardId());

                // DB 조회수에 Redis 조회수를 더해준다.
                board.combineRedisViewCount(redisViewCount);
            }
        }

        int totalCount =boardMapper.getBoardTotalCount(boardCode, categoryId, memberId, keyword,tagName);

        // 게시글이 없는 경우,
        if(boards == null || boards.isEmpty()){
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("list", new ArrayList<BoardResponse>());
            emptyResult.put("totalCount", 0);
            emptyResult.put("totalPages", 0);
            emptyResult.put("currentPage", page);
            return emptyResult;
        }

        List<BoardResponse> list = IntStream.range(0, boards.size())
                .mapToObj(i->{
                    Board board = boards.get(i);
                    long seqNumber = (long) totalCount - offset -i;
                    return new BoardResponse(board, seqNumber);
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        result.put("currentPage", page);
        return result;
    }
    /**
     * 페이징 처리가 된 게시글 목록을 조회합니다.
     * @param page 현재 페이지 번호
     * @param size 페이지당 게시글 수
     * @param categoryId 카테고리 (필터링용)
     * @return 목록 데이터와 페이징 정보를 담은 Map
     */
    @Override
    @Transactional
    public Map<String, Object> getBoardListWithPaging(int page ,int size ,String boardCode ,String categoryId ,String memberId ,String tagName) {
        // 마이너스 페이지 방지
        if(page < 1){
            page = 1;
        }

        int offset = (page -1 ) * size; // offset는 최소 0이 보장

        List<Board> boards = boardMapper.getBoardListWithPaging(offset, size, boardCode, categoryId,null, null, tagName);

        //[추가] 리스트의 각 항목에 Redis 조회수를 합산
        if(boards !=null && !boards.isEmpty()){
            for(Board board : boards){
                // 각 게시글 ID에 해당하는 Redis 조회수를 가져온다.
                int redisViewCount = viewCountRedisService.getViewCount(board.getBoardId());
                // DB 조회수에 Redis 조회수를 더해준다.
                board.combineRedisViewCount(redisViewCount);
            }
        }

        int totalCount =boardMapper.getBoardTotalCount(boardCode, categoryId, null, null,tagName);

        // 게시글이 없는 경우,
        if(boards == null || boards.isEmpty()){
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("list", new ArrayList<BoardResponse>());
            emptyResult.put("totalCount", 0);
            emptyResult.put("totalPages", 0);
            emptyResult.put("currentPage", page);
            return emptyResult;
        }

        // 게시글 있는 경우, 가상 번호 계산 및 세팅(stream 사용)
        List<BoardResponse> list = IntStream.range(0, boards.size())
                .mapToObj(i ->{
                    Board board = boards.get(i);
                    long seqNumber = (long) totalCount - offset -i;
                    return new BoardResponse(board, seqNumber);
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("totalPages",(int)  Math.ceil((double) totalCount/ size));
        result.put("currentPage", page);

        // 특정 사용자의 글 목록 조회 시, 댓글 수도 함께 포함(통계용)
        if(memberId != null && !memberId.isEmpty()) {
            int commentCount = memberMapper.getCommentCount(memberId);
            result.put("commentCount", commentCount);
        }
        return result;
    }

    @Override
    @Transactional
    public void incrementViewCount(Long boardId) {
         int result = boardMapper.existsBoardContent(boardId);
        if (result == 0) { // 게시글이 없는 경우
            throw new NotFoundContentBoardException();
        }

        // 기존 db 증가
        //int result = boardMapper.incrementViewCount(boardId);

        viewCountRedisService.incrViewCount(boardId);

    }

    /**
     * 특정 게시글을 삭제합니다.
     * @param boardIds 삭제할 게시글 ID
     * @return 삭제 성공 여부
     */
    @Override
    @Transactional // 게시글과 댓글 삭제 상태가 한 번에 보장되어야 함
    public boolean deleteBoards(List<Long> boardIds, String memberId, boolean isAdmin) {

        // 유효성 검사 (삭제할 ID 확인)
        if(boardIds == null || boardIds.isEmpty()){
            throw new MissingDeleteTargetException(); //해제할 스크랩 목록을 선택해주세요.
        }

        int updatedContent;

        if(isAdmin){
            // 관리자 & 슈퍼 관리자는 본인 여부 상관없이 단건 및 일괄 삭제 가능
            updatedContent = boardMapper.updateBoardDeleteYNByAdmin(boardIds, "Y");
        } else {
            // 게시판 소프트 삭제 (Delete_YN = 'Y) 일반사용자
             updatedContent = boardMapper.updateBoardDeleteYN(boardIds, memberId);
        }

        if(updatedContent == 0){
            // 단 한 건도 업데이트 되지 않았다면 (남의 글이거나 존재하지 않는 글)
                throw new NotBoardContentException(); //해당 게시판의 게시글을 찾을 수 없습니다.
        }

        // 연쇄 삭제: 해당 게시글들에 달린 모든 댓글도 보이지 않게 처리
        commentMapper.deleteCommentsByBoardIds(boardIds);

        // 파일 소프트 삭제
        boardMapper.updateFilesDeleteYnByBoardIds(boardIds);

        return true;
    }

    /**
     * 전문 검색(Full-Text Search)을 사용하여 게시글을 검색하고 페이징된 결과를 반환합니다.
     * @param keyword 검색어
     * @param boardCode 게시판 타입 (선택 사항)
     * @param page 현재 페이지
     * @param size 페이지당 개수
     * @return 검색 결과 및 페이징 정보
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> searchBoardContent(String keyword, String boardCode, int page, int size) {
        // 검색어가 비어있는지 확인
        if(keyword == null || keyword.trim().isEmpty()){
            throw new InvalidSearchKeywordException(); // 입력된 키워드가 없습니다.
        }

        // Boolean Mode에서 정확한 검색을 위해 검색어 가공      //keyword.trim() -> 검색어가 너무 엄격할 수 있음.
        String formattedKeyword = keyword.trim() + "*"; // 부분 일치 검색 활성화

        int offset = (page -1) * size;

        //DB 조회
        List<Board> boards = boardMapper.searchBoardContent(formattedKeyword, boardCode, offset,size);

        //[추가] 리스트의 각 항목에 Redis 조회수를 합산
        if(boards !=null && !boards.isEmpty()){
            for(Board board : boards){
                // 각 게시글 ID에 해당하는 Redis 조회수를 가져온다.
                int redisViewCount = viewCountRedisService.getViewCount(board.getBoardId());

                // DB 조회수에 Redis 조회수를 더해준다.
                board.combineRedisViewCount(redisViewCount);
            }
        }

        int totalCount = boardMapper.getSearchBoardCount(formattedKeyword, boardCode);

        // 결과가 없을 경우 빈 리스트([])를 반환하도록 보장
        List<BoardResponse> list = (boards !=null)
                ? boards.stream()
                        .map(BoardResponse::new)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        // 결과 구성( 페이징된 데이터 추가)
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / size)); //전체 페이지 수
        result.put("currentPage", page); // 현재 페이지

        return result;
    }// End Of searchBoardContent();

    /**
     * 추천을 토글(추가 또는 취소)합니다.
     * @param boardId
     * @param memberId
     * @return
     */
    @Override
    public boolean toggleLike(Long boardId, String memberId) {
        BoardResponse board = boardMapper.getSearchBoard(boardId,memberId);
        if (board == null) {
            throw new NotBoardContentException(); //해당 게시판의 게시글을 찾을 수 없습니다.
        }
        // 이미 추천했는지 확인
        int count = boardMapper.checkBoardLike(boardId, memberId);

        if (count == 0) { // 추천 안 했으면 추가 (1증가)
            boardMapper.insertBoardLike(boardId, memberId);
            boardMapper.updateLikeCount(boardId, 1);
            return true;
        } else { // 이미 추천한 경우 (1 감소)
            boardMapper.deleteBoardLike(boardId, memberId);
            boardMapper.updateLikeCount(boardId, -1);
            return false;
        }
    }


    /* ========================================================================= */
    /*                          private method */
    /* ========================================================================= */

    /**
     * 카테고리 유효성 검증
     * @param boardCode
     * @param categoryId
     */
    private void isValidateCategoryForBoard(String boardCode, String categoryId){
        if(!boardMapper.isValidateCategoryForBoard(boardCode, categoryId)){
            throw new InvalidCategoryException(); //유효하지 않은 카테고리입니다
        }
    }

    /**
     * boardCode 존재 여부를 검증한다.
     * @param boardCode
     */
    private void validateBoardCodeExists(String boardCode) {
        if(!boardMasterMapper.validateBoardCodeExist(boardCode)){
            throw new NotFoundBoardCodeException();
        }
    }
}
