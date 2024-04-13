package org.itmo.eventapp.main.security.securityexpression;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class ParticipantsSecurityExpression {
    private final MiscSecurityExpression miscSecurityExpression;


    public boolean canImportList(@Min(value = 1, message = "Параметр eventId не может быть меньше 1") int eventId) {
        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.IMPORT_PARTICIPANT_LIST_XLSX);
    }


    public boolean canWorkWithList(@Min(value = 1, message = "Параметр eventId не может быть меньше 1") int eventId) {
        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.WORK_WITH_PARTICIPANT_LIST);
    }

    public boolean canExportList(@Min(value = 1, message = "Параметр eventId не может быть меньше 1") int eventId) {
        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.EXPORT_PARTICIPANT_LIST_XLSX);
    }
}