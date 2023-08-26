package com.jca.sanction.controller;

import com.jca.sanction.dto.ActionRequest;
import com.jca.sanction.dto.SanctionAction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.service.ActionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class ActionController {

    private final ActionService actionService;

    @RequestMapping(method = RequestMethod.POST, value = "/sanctions/{sanctionId}/activate")
    public ResponseEntity<List<SanctionAction>> activateSanction(@PathVariable(name = "sanctionId")String sanctionId,
                                                                 @RequestBody SanctionEventAction sanctionEventAction){
        return ResponseEntity.ok(actionService.activateSanction(sanctionId, sanctionEventAction));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sanctions/{sanctionId}/approve")
    public ResponseEntity<List<SanctionAction>> approveSanction(@PathVariable(name = "sanctionId")String sanctionId,
                                                                @RequestBody ActionRequest actionRequest) {
        return ResponseEntity.ok(actionService.approveSanction(sanctionId,actionRequest.getNote(), "approvedAdmin"));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sanctions/{sanctionId}/reject")
    public ResponseEntity<List<SanctionAction>> rejectSanction(@PathVariable(name = "sanctionId")String sanctionId,
                                                                @RequestBody ActionRequest actionRequest) {
        return ResponseEntity.ok(actionService.rejectSanction(sanctionId,actionRequest.getNote(), "approvedAdmin"));
    }
}
