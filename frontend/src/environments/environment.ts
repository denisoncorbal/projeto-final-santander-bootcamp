import { LogLevel } from "src/app/constants/log-level";

export const environment = {
    webProtocol: "http://",
    backendBasePath: "localhost",
    backendPort: ":8080",
    backendVersion: "v1",
    logLevel: LogLevel.ERROR
};
