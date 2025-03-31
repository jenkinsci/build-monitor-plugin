import { BuildStatus } from "./Build";

export interface Stage {
  id: string;
  name: string;
  startTimeMillis: number;
  status: BuildStatus;
  durationMillis: number;
  pauseDurationMillis: number;
  stageFlowNodes: {
    _links: {
      console: {
        href: string;
      };
      self: {
        href: string;
      };
      log: {
        href: string;
      };
    };
  }[];
}
