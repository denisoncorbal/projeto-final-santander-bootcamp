import { RegisterClass } from "./register-class";
import { RegisterUser } from "./register-user";

export interface Register {
    id?: number,
    date: Date,
    registerValue: number,
    type: string,
    registerUser: RegisterUser | null,
    registerClass: RegisterClass | null
}
