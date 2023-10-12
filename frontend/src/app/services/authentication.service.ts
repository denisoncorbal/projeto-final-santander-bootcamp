import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, firstValueFrom } from 'rxjs';
import { BackendRoutes } from '../constants/backend-routes';
import { RegisterUser } from '../model/register-user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  private httpOptions = {
    headers:
      new HttpHeaders({
        "Content-Type":"application/json",
        //Authorization:"Basic " + btoa("denison.corbal@gmail.com:123456")
      })    
  };

  private actualUser: RegisterUser = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    registers: []
  };

  tryAthenticate(email: string, password: string){
    return this.readUser(email).subscribe({
      next: (value)=>{
        this.actualUser = value;
      }
    })
  };

  isAuthenticated(){
    if(!this.actualUser.id)
      return false;
    return true;
  };

  // USER
  // create
  createUser(user: RegisterUser): Observable<RegisterUser>{
    return this.http.post<RegisterUser>(BackendRoutes.USER, JSON.stringify(user), this.httpOptions);
  }
  // readAll
  private readUsers(): Observable<RegisterUser[]>{
    return this.http.get<RegisterUser[]>(BackendRoutes.USER);
  }
  // read
  private readUser(email: string){
    return this.http.get<RegisterUser>(BackendRoutes.USER + "/" + email);
  }
  // update
  private updateUser(id: number, user: RegisterUser):Observable<RegisterUser>{
    return this.http.put<RegisterUser>(BackendRoutes.USER + "/" + id, JSON.stringify(user), this.httpOptions);
  }
  // delete
  private deleteUser(id: number):Observable<void>{
    return this.http.delete<void>(BackendRoutes.USER + "/" + id);
  }
}
