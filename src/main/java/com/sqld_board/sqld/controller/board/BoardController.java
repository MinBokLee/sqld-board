package com.sqld_board.sqld.controller.board;

import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.dto.request.board.BoardDeleteRequest;
import com.sqld_board.sqld.dto.request.board.BoardRequest;
import com.sqld_board.sqld.dto.request.board.CommentRequest;
import com.sqld_board.sqld.dto.request.board.ScrapDeleteRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.common.LoginRequiredException;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.model.board.BoardFile;
import com.sqld_board.sqld.model.board.Comment;
import com.sqld_board.sqld.service.board.BoardServiceImpl;
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

import static com.sqld_board.sqld.dto.response.Response.success;

/**
 * 게시판(Board) 및 댓글(Comment) 관련 API 엔드포인트를 처리하는 컨트롤러 클래스입니다.
 * 모든 경로는 프론트엔드와 호환되도록 원래대로 복구되었습니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardServiceImpl boardService;
    private final ResponseHandler responseHandler;

    @Operation(summary="스크랩 페이지 삭제")
    @DeleteMapping("/deleteMyScrapPage")
    public ResponseEntity<Response> deleteMyScrapPage(@RequestBody ScrapDeleteRequest scrapDeleteReq, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.deleteMyScrapPage(scrapDeleteReq.getScrapIds(), user.getUsername());
        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.BOARD_SCRAP_DELETE_SUCCESS));
    }

    @Operation(summary = "스크랩 페이지 조회")
    @GetMapping("/searchScrapMyPage")
    public ResponseEntity<Response> searchScrapMyPage(@RequestParam String keyword, @AuthenticationPrincipal User user){
        if (user == null) throw new LoginRequiredException();
        List<BoardResponse> resultData = boardService.searchScrapMyPage(keyword.trim(), user.getUsername());
        return ResponseEntity.ok(success(resultData));
    }

    @Operation(summary = "스크랩 추가/취소(토글)")
    @PostMapping("/insertBoardScrap")
    public ResponseEntity<Response> insertBoardScrap(@RequestParam Long boardId, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boolean isScrapped = boardService.insertBoardScrap(boardId, user.getUsername());
        return ResponseEntity.ok(responseHandler.getSuccessResponse(isScrapped ? MessageType.BOARD_SCRAP_INSERT_SUCCESS : MessageType.BOARD_SCRAP_DELETE_SUCCESS));
    }

    @Operation(summary = "인기 게시글 조회")
    @GetMapping("/popularBoards")
    public ResponseEntity<Response> getPopularBoards(){
        return ResponseEntity.ok(success(boardService.getPopularBoards()));
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
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.deleteComment(commentId, user.getUsername());
        return ResponseEntity.ok(success(MessageConstants.DELETE_OK));
    }

    @Operation(summary="게시판 댓글 수정")
    @PutMapping("/modifyComment")
    public ResponseEntity<Response> modifyComment(@RequestParam Long commentId, @RequestParam String content, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        boardService.updateComment(commentId, content, user.getUsername());
        return ResponseEntity.ok(success(MessageConstants.UPDATE_OK));
    }

    @Operation(summary="게시판 댓글 확인")
    @GetMapping("/readComment")
    public ResponseEntity<Response> readComment(@RequestParam Long boardId) {
        List<Comment> result = boardService.getCommentList(boardId);
        return ResponseEntity.ok(success(result));
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
    public Response updateBoard(@PathVariable Long boardId, @ModelAttribute BoardRequest boardReq, @AuthenticationPrincipal User user) {
        String memberId = (user != null) ? user.getUsername() : null;
        boardService.updateBoard(boardId, boardReq, memberId);
        if(boardReq.getDeleteFileIds() != null && !boardReq.getDeleteFileIds().isEmpty()){
            boardService.deleteFiles(boardReq.getDeleteFileIds());
        }
        if (boardReq.getFiles() != null && !boardReq.getFiles().isEmpty()) {
            boardService.uploadFiles(boardId, boardReq.getFiles());
        }
        return success(MessageConstants.UPDATE_OK);
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
    public Response searchContent(@RequestParam String keyword, @RequestParam(required = false) String boardType, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = boardService.searchBoardContent(keyword.trim(), boardType, page, size);
        return success(result);
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
        return success(result);
    }

    @Operation(summary = "내가 쓴 글의 리스트 조회")
    @GetMapping("/my-list")
    public ResponseEntity<Response> getBoardMyList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String boardType, @RequestParam(required = false) String category, @RequestParam(required = false) String tagName, @AuthenticationPrincipal User user){
        if(user == null) throw new LoginRequiredException();
        Map<String, Object> result = boardService.getBoardMyList(page, size, boardType, category, tagName, user.getUsername());
        return ResponseEntity.ok(Response.success(result));
    }

    @Operation(summary = "게시판 목록 페이징 조회")
    @GetMapping("/list/paging")
    public ResponseEntity<Response> getBoardListWithPaging(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String boardType, @RequestParam(required = false) String category, @RequestParam(required = false) String memberId, @RequestParam(required = false) String tagName) {
        Map<String, Object> result = boardService.getBoardListWithPaging(page, size, boardType, category, memberId, tagName);
        return ResponseEntity.ok(success(result));
    }

    @Operation(summary = "게시판 목록 읽기")
    @GetMapping("/list")
    public ResponseEntity<Response> getBoardList() {
        List<BoardResponse> result = boardService.getBoardList();
        return ResponseEntity.ok(success(result));
    }

    @Operation(summary = "게시글 조회수 증가")
    @PutMapping("/countView")
    public ResponseEntity<Response> countView(@RequestParam Long boardId) {
        boardService.incrementViewCount(boardId);
        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.View_Increment_Success));
    }

    @Operation(summary = "게시판 글 삭제(소프트)")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/list/deleteBoardContent")
    public Response deleteBoards(@RequestBody BoardDeleteRequest boardDeleteRequest, @AuthenticationPrincipal User user) {
        if(user == null) throw new LoginRequiredException();
        boardService.deleteBoards(boardDeleteRequest.getBoardIds(), user.getUsername());
        return responseHandler.getSuccessResponse(MessageType.DELETE_CONTENT_Y);
    }
}
