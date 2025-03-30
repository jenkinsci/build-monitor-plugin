interface StageThing {
    id: string;
    name: string;
    durationMillis: number;
    status: BuildStatus;
}

export interface Build {
    id: string;
    name: string;
    startTimeMillis: number;
    status: BuildStatus;
    stages: StageThing[];
}

export type BuildStatus = 'NOT_EXECUTED' |
    'ABORTED' |
    'SUCCESS' |
    'IN_PROGRESS' |
    'PAUSED_PENDING_INPUT' |
    'FAILED' |
    'UNSTABLE';
