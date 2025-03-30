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
    'aborted' |
    'successful' |
    'IN_PROGRESS' |
    'failing' |
    'UNSTABLE';
