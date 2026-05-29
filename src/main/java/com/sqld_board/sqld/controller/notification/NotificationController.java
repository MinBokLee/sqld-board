package com.sqld_board.sqld.controller.notification;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.webSocket.NotificationResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.signature.qual.BinaryNameOrPrimitiveType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final ResponseHandler responseHandler;

    @Operation(summary = "모든 알림 읽음 확인")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/readAll/{receiverId}")
    public Response updateAllNotiRead(@PathVariable String receiverId){
        notificationService.updateAllNotiRead(receiverId);
        return responseHandler.getSuccessResponse(MessageType.NOTIC_CONTENTS_CHECK_OK);
    }

    @Operation(summary = "공지 읽음 확인")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/read/{notiId}")
    public Response updateNotiRead(@PathVariable long notiId) {
        notificationService.updateNotiRead(notiId);

        return responseHandler.getSuccessResponse(MessageType.NOTIC_CONTENTS_CHECK_OK);
    }

    @Operation(summary = "알림 내역 리스트 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/{receiverId}")
    public ResponseEntity<Object> searchNotification(@PathVariable String receiverId) {

        List<NotificationResponse> notiList =  notificationService.searchNotification(receiverId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("code", 200);
        response.put("msg", "success");
        response.put("data", notiList); // result 계층 제거

        return ResponseEntity.ok(response);
    }

}
