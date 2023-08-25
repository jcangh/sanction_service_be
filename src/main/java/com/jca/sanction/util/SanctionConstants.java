package com.jca.sanction.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SanctionConstants {

    public static final String MESSAGE_HEADER_REQUIRED_APPROVALS = "requiredApprovals";

    public static final String MESSAGE_HEADER_REQUIRED_REJECTIONS = "requiredRejections";

    public static final String MESSAGE_HEADER_APPROVALS_COUNT = "approvalsCount";

    public static final String MESSAGE_HEADER_REJECTIONS_COUNT = "rejectionsCount";

    public static final int SANCTION_DEFAULT_REQUIRED_APPROVALS = 2;

    public static final int SANCTION_DEFAULT_REQUIRED_REJECTIONS = 1;

    public static final String MESSAGE_HEADER_SANCTION_ID = "sanctionId";

    public static final String MESSAGE_HEADER_NOTE = "note";

    public static final String MESSAGE_HEADER_ACTION_STATE_BY = "actionStateBy";

    public static final String MESSAGE_HEADER_UPDATED_SANCTION = "updatedSanction";

    public static final String REQUIRED_APPROVALS = "requiredApprovals";

    public static final String REQUIRED_REJECTIONS = "requiredRejections";
}
