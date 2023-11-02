import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BackendRoutes } from '../constants/backend-routes';
import { RegisterUser } from '../model/register-user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }  

  private actualUser: RegisterUser = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    registers: [],
    accessToken: '',
    refreshToken: ''
  };

  private httpOptions = {
    headers:
      new HttpHeaders({
        "Content-Type":"application/json",
      })    
  };

  getAccessToken(){
    return this.actualUser.accessToken || '';
  }

  getRefreshToken(){
    return this.actualUser.refreshToken || '';
  }

  tryAthenticate(email: string, password: string){
    this.actualUser.email = email;
    this.actualUser.password = password;
    return this.login(this.actualUser).subscribe({
      next: (value)=>{
        this.actualUser.accessToken = value.accessToken;
        this.actualUser.refreshToken = value.refreshToken;
      }
    })
  };

  tryRefresh(){
    return this.refresh().subscribe({
      next: (user)=>{
        this.actualUser.accessToken = user.accessToken;
        this.actualUser.refreshToken = user.refreshToken;
      },
      error: (error)=>{
        this.actualUser.accessToken = '';
        this.actualUser.refreshToken = '';
      }
    })
  }

  isAuthenticated(){
    if(!this.actualUser.accessToken)
      return false;
    return true;
  };

  getActualEmail(): string{
    return this.actualUser.email;
  }

  logout(){
    this.actualUser = {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      registers: [],
      accessToken: '',
      refreshToken: ''
    };
  }

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
  // LOGIN
  private login(user: RegisterUser): Observable<RegisterUser>{
    return this.http.post<RegisterUser>(BackendRoutes.AUTH + '/login', JSON.stringify(user), this.httpOptions);
  }
  private refresh(): Observable<RegisterUser>{
    return this.http.post<RegisterUser>(BackendRoutes.AUTH + '/refresh', JSON.stringify(this.actualUser), this.httpOptions);
  }
}
