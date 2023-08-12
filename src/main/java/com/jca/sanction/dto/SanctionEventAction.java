package com.jca.sanction.dto;

public record SanctionEventAction (Integer requiredApprovals, Integer requiredRejections, String note, String actionBy){}
