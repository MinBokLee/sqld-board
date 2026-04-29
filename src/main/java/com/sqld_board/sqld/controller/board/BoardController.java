package com.sqld_board.sqld.controller.board;

import com.sqld_board.sqld.dto.request.board.BoardDeleteRequest;
import com.sqld_board.sqld.dto.request.board.BoardRequest;
import com.sqld_board.sqld.dto.request.board.CommentModifyRequest;
import com.sqld_board.sqld.dto.request.board.CommentRequest;
import com.sqld_board.sqld.dto.request.board.ScrapDeleteRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.dto.response.code.CategoryResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.common.LoginRequiredException;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.model.board.BoardFile;
import com.sqld_board.sqld.model.commentManagement.CommentManagement;
import com.sqld_board.sqld.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시판(Board) 및 댓글(Comment) 관련 API 엔드포인트를 처리하는 컨트롤러 클래스입니다.
 * 모든 경로는 프론트엔드와 호환되도록 원래대로 복구되었습니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseHandler responseHandler;

    @Operation(summary = "카테고리 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{boardCode}/categories")
    public Response getCategoryListByBoardCode(@PathVariable String boardCode) {
        List<CategoryResponse> categoryData = boardService.getCategoryListByBoardCode(boardCode);

        return Response.success(categoryData);
    }

    @Operation(summary="스크랩 페이지 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteMyScrapPage")
    public Response deleteMyScrapPage(@RequestBody ScrapDeleteRequest scrapDeleteReq, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.deleteMyScrapPage(scrapDeleteReq.getScrapIds(), user.getUsername());

        return responseHandler.getSuccessResponse(MessageType.BOARD_SCRAP_DELETE_SUCCESS);
    }

    @Operation(summary = "스크랩 페이지 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searchScrapMyPage")
    public Response searchScrapMyPage(@RequestParam (defaultValue = "1") int page
                                                     ,@RequestParam (defaultValue = "10") int size
                                                     ,@RequestParam (required = false, defaultValue = "") String keyword
                                                     ,@AuthenticationPrincipal User user){
        if (user == null) throw new LoginRequiredException();

        if(size < 1) size = 10;
        if(size > 100) size = 100;

        Map<String, Object> resultData = boardService.searchScrapMyPage(page, size, keyword.trim(), user.getUsername());
        return Response.success(resultData);
    }

    @Operation(summary = "스크랩 추가/취소(토글)")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/insertBoardScrap")
    public Response insertBoardScrap(@RequestParam Long boardId, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boolean isScrapped = boardService.insertBoardScrap(boardId, user.getUsername());

        return responseHandler.getSuccessResponse(isScrapped ? MessageType.BOARD_SCRAP_INSERT_SUCCESS : MessageType.BOARD_SCRAP_DELETE_SUCCESS);
    }

    @Operation(summary = "인기 게시글 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popularBoards")
    public Response getPopularBoards(){
        List<BoardResponse> popularBoards  = boardService.getPopularBoards();

        return Response.success(popularBoards);
    }

    @Operation(summary = "첨부 파일 다운로드")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        Resource resource = boardService.downloadFile(fileId);
        BoardFile fileInfo = boardService.getBoardFileInfo(fileId);
        String encodeFileName = URLEncoder.encode(fileInfo.getOriginName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeFileName + "\"" )
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }

    @Operation(summary = "게시판 댓글 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteComment/{commentId}")
    public Response deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.deleteComment(commentId, user.getUsername());
        return responseHandler.getSuccessResponse(MessageType.DELETE_COMMENT);
    }

    @Operation(summary="게시판 댓글 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/modifyComment")
    public Response modifyComment(@RequestBody CommentModifyRequest request, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.updateComment(request.getCommentId(), request.getContent(), user.getUsername());

        return responseHandler.getSuccessResponse(MessageType.UPDATE_COMMENT);
    }

    @Operation(summary="게시판 댓글 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/readComment")
    public Response readComment(@RequestParam Long boardId) {
        List<CommentManagement> boardComment = boardService.getCommentList(boardId);

        return Response.success(boardComment);
    }

    @Operation(summary="게시판 댓글 작성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/writeComment")
    public Response writeComment(@RequestBody CommentRequest request, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.writeComment(request, user.getUsername());
        return responseHandler.getSuccessResponse(MessageType.CREATE_COMMENT);
    }

    @Operation(summary = "게시판 글 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/list/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response updateBoard(@PathVariable Long boardId, @ModelAttribute BoardRequest request, @AuthenticationPrincipal User user) {

        // 1. 현재 로그인한 사용자의 ID(username)를 추출합니다. (로그인하지 않은 경우 null)
        String memberId = (user != null) ? user.getUsername() : null;

        boardService.updateBoard(boardId, request, memberId);

        // 사용자가 삭제를 요청한 기존 첨부 파일들이 있다면 실제 파일과 DB 레코드를 삭제합니다.
        if(request.getDeleteFileIds() != null && !request.getDeleteFileIds().isEmpty()){
            boardService.deleteFiles(request.getDeleteFileIds());
        }

        // 새로 업로드할 첨부 파일이 있는 경우 서버에 저장하고 DB에 등록합니다.
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            boardService.uploadFiles(boardId, request.getFiles());
        }

        return responseHandler.getSuccessResponse(MessageType.UPDATE_BOARD_CONTENT_SUCCESS);
    }

    @Operation(summary ="게시판 글 작성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/list/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response writeBoard(@ModelAttribute BoardRequest request, @AuthenticationPrincipal User user) {

        if(user == null) throw new LoginRequiredException();
        Long boardId = boardService.writeBoard(request, user.getUsername());

        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            boardService.uploadFiles(boardId, request.getFiles());
        }
        return responseHandler.getSuccessResponse(MessageType.Create_Board_Content);
    }

    @Operation(summary = "CKEditor 이미지 업로드")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadImages(@RequestPart(value = "upload", required = false) MultipartFile upload) {
        Map<String, Object> result = new HashMap<>();
        try {
            String url = boardService.uploadImage(upload);
            result.put("uploaded",true);
            result.put("url",url);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message" , "파일 업로드 실패:"+ e.getMessage());
            result.put("uploaded", false);
            result.put("error",error);
        }
        return result;
    }

    @Operation(summary = "게시글 전문 검색(FTS)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/searchContent")
    public Response searchContent(@RequestParam(required = false, defaultValue = "") String keyword, @RequestParam(required = false) String boardCode, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> searchData = boardService.searchBoardContent(keyword.trim(), boardCode, page, size);

        return Response.success(searchData);
    }

    @Operation(summary = "게시글 추천 토글")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/like")
    public Response toggleLike(@RequestParam Long boardId, @AuthenticationPrincipal User user) {
        if(user == null) throw new LoginRequiredException();
        boolean isLiked = boardService.toggleLike(boardId, user.getUsername());

        return responseHandler.getSuccessResponse(isLiked ? MessageType.IsLiked_Content : MessageType.IsNotLiked_Content);
    }

    @Operation(summary = "게시판 단건 조회 (상세)")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/detail/{boardId}")
    public Response getSearchBoard(@PathVariable Long boardId, @AuthenticationPrincipal User user){
        String memberId = (user != null) ? user.getUsername() : null;
        BoardResponse result = boardService.getSearchBoard(boardId, memberId);
        return Response.success(result);
    }

    @Operation(summary = "내가 쓴 글의 리스트 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my-list")
    public Response getBoardMyList(@RequestParam(defaultValue = "1") int page
                                                  ,@RequestParam(defaultValue = "10") int size
                                                  ,@RequestParam(required = false) String boardCode
                                                  ,@RequestParam(required = false) String categoryId
                                                  ,@RequestParam(required = false) String tagName
                                                  ,@RequestParam(required = false, defaultValue = "") String keyword
                                                  , @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();

        // 페이지당 개수 제한 (서버 보호)
        if(size < 1) size = 10;   // 1보다 작으면 기본 값을 10으로
        if(size >100) size = 100; // 최대 100개까지만 허용

        String memberId = (user != null) ? user.getUsername() : null;
        Map<String, Object> myContentDataList = boardService.getBoardMyList(page, size, boardCode, categoryId, tagName, keyword.trim(), memberId);
        return Response.success(myContentDataList);
    }

    @Operation(summary = "게시판 목록 페이징 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/paging")
    public Response getBoardListWithPaging(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String boardCode, @RequestParam(required = false) String categoryId, @RequestParam(required = false) String memberId, @RequestParam(required = false) String tagName) {
        Map<String, Object> pagingData = boardService.getBoardListWithPaging(page, size, boardCode, categoryId, memberId, tagName);

        return Response.success(pagingData);
    }

    @Operation(summary = "게시판 목록 읽기")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    public Response getBoardList() {
        List<BoardResponse> boardList = boardService.getBoardList();
        return Response.success(boardList);
    }

    // Redis 타입으로 수정 완료함 ( 이 API는 로그용으로 남겨둠)
    /*
    @Operation(summary = "게시글 조회수 증가")
    @PutMapping("/countView")
    public ResponseEntity<Response> countView(@RequestParam Long boardId) {
        boardService.incrementViewCount(boardId);
        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.View_Increment_Success));
    } */

    @Operation(summary = "게시판 글 삭제(소프트)")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/list/deleteBoardContent")
    public Response deleteBoards(@RequestBody BoardDeleteRequest boardDeleteRequest, @AuthenticationPrincipal User user) {

        if(user == null) throw new LoginRequiredException(); //로그인이 필요한 서비스 입니다.

        // ROLE_ADMIN OR ROLE_SUPER_ADMIN 권한이 있는지 확인
        boolean isAdmin = user.getAuthorities()
                              .stream()
                              .anyMatch(a ->a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        boardService.deleteBoards(boardDeleteRequest.getBoardIds(), user.getUsername(), isAdmin);

        return responseHandler.getSuccessResponse(MessageType.DELETE_CONTENT_Y);
    }
}
