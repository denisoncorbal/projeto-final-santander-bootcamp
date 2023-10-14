import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BackendRoutes } from '../constants/backend-routes';
import { Register } from '../model/register';
import { RegisterClass } from '../model/register-class';
import { RegisterUser } from '../model/register-user';

@Injectable({
  providedIn: 'root'
})
export class BackendDataService {

  private httpOptions = {
    headers:
      new HttpHeaders({
        "Content-Type":"application/json",
        //Authorization:"Basic " + btoa("denison.corbal@gmail.com:123456")
      })    
  };
  constructor(private http: HttpClient) { }  

  // CLASS
  // create
  createClass(registerClass: RegisterClass){
    return this.http.post<RegisterClass>(BackendRoutes.CLASS, JSON.stringify(registerClass), this.httpOptions);
  }
  // read
  readClasses(){
    return this.http.get<RegisterClass[]>(BackendRoutes.CLASS);
  }
  readClassesByUser(userEmail: string){
    return this.http.get<RegisterClass[]>(BackendRoutes.CLASS + "/" + userEmail);
  }
  // update
  updateClass(id: number, registerClass: RegisterClass){
    return this.http.put<RegisterClass>(BackendRoutes.CLASS + "/" + id, JSON.stringify(registerClass), this.httpOptions);
  }
  // delete
  deleteClass(id: number){
    return this.http.delete<void>(BackendRoutes.CLASS + "/" + id);
  }

  // REGISTER
  // create
  createRegister(register: Register){
    return this.http.post<Register>(BackendRoutes.REGISTER, JSON.stringify(register), this.httpOptions);
  }
  // read
  readRegisters(){
    return this.http.get<Register[]>(BackendRoutes.REGISTER);
  }
  readRegistersByUser(userEmail: string){
    return this.http.get<Register[]>(BackendRoutes.REGISTER + "/" + userEmail);
  }
  // update
  updateRegister(id: number, register: Register){
    return this.http.put<Register>(BackendRoutes.REGISTER + "/" + id, JSON.stringify(register), this.httpOptions);
  }
  // delete
  deleteRegister(id: number){
    return this.http.delete<void>(BackendRoutes.REGISTER + "/" + id);
  }
  
  // ASSOCIATION  
  // register to user and class
  associateRegister(registerId: number, userEmail: string, className: string){
    return this.http.post<Register>(BackendRoutes.ASSOCIATION + "/register/" + registerId + "?userEmail=" + userEmail + "&className=" + className, this.httpOptions);
  }
  // user to class
  associateClass(classId: number, userEmail: string){
    return this.http.post<Register>(BackendRoutes.ASSOCIATION + "/class/" + classId + "?userEmail=" + userEmail, this.httpOptions);
  }
}