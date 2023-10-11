import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterUser } from '../model/register-user';
import { BackendRoutes } from '../constants/backend-routes';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BackendDataService {

  private headers = {
    "Content-Type":"application/json"
  };

  constructor(private http: HttpClient) { }

  // USER
  // create
  createUser(user: RegisterUser): Observable<RegisterUser>{
    return this.http.post<RegisterUser>(BackendRoutes.USER, JSON.stringify(user), {headers: this.headers});
  }
  // read
  readUsers(): Observable<RegisterUser[]>{
    return this.http.get<RegisterUser[]>(BackendRoutes.USER);
  }
  // update
  updateUser(id: number, user: RegisterUser):Observable<RegisterUser>{
    return this.http.put<RegisterUser>(BackendRoutes.USER + "/" + id, JSON.stringify(user));
  }
  // delete
  deleteUser(id: number):Observable<void>{
    return this.http.delete<void>(BackendRoutes.USER + "/" + id);
  }

  // CLASS
  // create
  createClass(){}
  // read
  readClass(){}
  // update
  updateClass(){}
  // delete
  deleteClass(){}

  // REGISTER
  // create
  createRegister(){}
  // read
  readRegister(){}
  // update
  updateRegister(){}
  // delete
  deleteRegister(){}
  
  // ASSOCIATION
  associateRegister(){}
}