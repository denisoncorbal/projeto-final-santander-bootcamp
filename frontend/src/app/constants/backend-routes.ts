const backendBasePath = "http://localhost"
const backendPort = "8080"
const backendVersion = "v1"
const backendFullPath = backendBasePath + ":" + backendPort + "/api/" + backendVersion;
export enum BackendRoutes {
    REGISTER = backendFullPath + "/register",
    USER = backendFullPath + "/user",
    CLASS = backendFullPath + "/class",
    ASSOCIATION = backendFullPath + "/association"
}
