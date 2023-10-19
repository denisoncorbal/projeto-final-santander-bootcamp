import { environment } from "src/environments/environment";

const webProtocol = environment.webProtocol; 
const backendBasePath = environment.backendBasePath;
const backendPort = environment.backendPort;
const backendVersion = environment.backendVersion;
const backendFullPath = webProtocol + backendBasePath + backendPort + "/api/" + backendVersion;

export const BackendRoutes = {
    REGISTER: backendFullPath + "/register",
    USER: backendFullPath + "/user",
    CLASS: backendFullPath + "/class",
    ASSOCIATION: backendFullPath + "/association"
}
