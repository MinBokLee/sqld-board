package com.sqld_board.sqld.controller.notification;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.webSocket.NotificationResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 읽음 확인
     * @param notiId
     * @return
     */
    @PutMapping("/read/{notiId}")
    public ResponseEntity<Response> updateNotiRead(@PathVariable long notiId) {
        notificationService.updateNotiRead(notiId);

        return ResponseEntity.ok(Response.success(MessageType.NOTIC_CONTENTS_CHECK_OK));
    }

    /**
     * 알림 내역 리스트 조회
     * @param receiverId
     * @return
     */
    @GetMapping("/list/{receiverId}")
    public ResponseEntity<Response> searchNotification(@PathVariable String receiverId) {

        List<NotificationResponse> notiList =  notificationService.searchNotification(receiverId);
        return ResponseEntity.ok(Response.success(notiList));
    }

}
