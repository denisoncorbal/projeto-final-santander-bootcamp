import { RegisterClass } from "./register-class";
import { RegisterUser } from "./register-user";

export interface Register {
    id?: number,
    date: Date,
    registerValue: number,
    registerType: string,
    registerUser: RegisterUser,
    registerClass: RegisterClass
}
