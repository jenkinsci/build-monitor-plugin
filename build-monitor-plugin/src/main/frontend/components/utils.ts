import {BuildStatus} from "../models/Build";

export function buildStatusToClass(status: BuildStatus) {
    const map: {[key in BuildStatus]: string} = {
        'NOT_EXECUTED': '--null',
        'ABORTED': '--null',
        'SUCCESS': '--success',
        'IN_PROGRESS': '--in-progress',
        'PAUSED_PENDING_INPUT': '--null',
        'FAILED': '--destructive',
        'UNSTABLE': '--warning'
    }

    return map[status];
}
