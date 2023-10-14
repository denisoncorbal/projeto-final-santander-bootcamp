import { Register } from "./register";

export interface RegisterUser {
    id?: number,
    firstName: string,
    lastName: string,
    email: string,
    password: string,    
    registers: Register[]
}
