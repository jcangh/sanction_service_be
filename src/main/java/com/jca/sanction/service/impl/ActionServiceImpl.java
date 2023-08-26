package com.jca.sanction.service.impl;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.SanctionAction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.mapper.SanctionMapper;
import com.jca.sanction.service.ActionService;
import com.jca.sanction.service.SanctionService;
import com.jca.sanction.statemachine.StateMachineEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.jca.sanction.util.SanctionConstants.*;

@Slf4j
@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    private final SanctionService sanctionService;
    private final StateMachineEventService eventService;

    private final SanctionMapper sanctionMapper;

    @Override
    public List<SanctionAction> activateSanction(String sanctionId, SanctionEventAction action) {
        eventService.activateSanctionEvent(sanctionId, action);
        SanctionEntity entity = sanctionService.getById(sanctionId);
        return entity.getActions().stream().map(sanctionMapper::toSanctionAction).toList();
    }

  @Override
  public List<SanctionAction> approveSanction(String sanctionId, String note, String approvedBy) {
    return processEvent(sanctionId, SanctionEvent.APPROVE, note, approvedBy);
  }

    @Override
    public List<SanctionAction> rejectSanction(String sanctionId, String note, String approvedBy) {
        return processEvent(sanctionId, SanctionEvent.REJECT, note, approvedBy);
    }

  private List<SanctionAction> processEvent(
      String sanctionId, SanctionEvent sanctionEvent, String note, String approvedBy) {
    Map<String, Object> additionalValues = sanctionService.getAdditionalValues(sanctionId);
    int requiredApprovals =
        additionalValues != null
            ? (int) additionalValues.get(REQUIRED_APPROVALS)
            : SANCTION_DEFAULT_REQUIRED_APPROVALS;
    int requiredRejections =
        additionalValues != null
            ? (int) additionalValues.get(REQUIRED_REJECTIONS)
            : SANCTION_DEFAULT_REQUIRED_REJECTIONS;

    var approvalsCount = sanctionService.getEventCount(sanctionEvent) + 1;

    if (SanctionEvent.APPROVE == sanctionEvent) {
      eventService.approveSanctionEvent(
          sanctionId,
          new SanctionEventAction(requiredApprovals, requiredRejections, note, approvedBy),
          approvalsCount);
    } else if (SanctionEvent.REJECT == sanctionEvent) {
      eventService.rejectSanctionEvent(
          sanctionId,
          new SanctionEventAction(requiredApprovals, requiredRejections, note, approvedBy),
          approvalsCount);
    }
    return sanctionService.getById(sanctionId).getActions().stream()
        .map(sanctionMapper::toSanctionAction)
        .toList();
  }
}
