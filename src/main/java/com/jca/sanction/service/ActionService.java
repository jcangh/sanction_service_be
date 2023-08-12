package com.jca.sanction.service;

import com.jca.sanction.dto.SanctionAction;
import com.jca.sanction.dto.SanctionEventAction;

import java.util.List;

public interface ActionService {

    List<SanctionAction> activateSanction(String sanctionId, SanctionEventAction action);
}
