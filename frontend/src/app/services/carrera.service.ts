import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Carrera, CarreraRequest } from '../models/carrera.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarreraService {
  private readonly apiUrl = `${environment.apiUrl}/carreras`;

  constructor(private http: HttpClient) {}

  // Obtener todas las carreras
  getCarreras(soloActivas = false): Observable<Carrera[]> {
    const params = new HttpParams().set('soloActivas', soloActivas.toString());
    return this.http.get<Carrera[]>(this.apiUrl, { params });
  }

  // Obtener carrera por ID
  getCarreraById(id: number): Observable<Carrera> {
    return this.http.get<Carrera>(`${this.apiUrl}/${id}`);
  }

  // Obtener carreras por facultad
  getCarrerasByFacultad(facultadId: number, soloActivas = false): Observable<Carrera[]> {
    const params = new HttpParams().set('soloActivas', soloActivas.toString());
    return this.http.get<Carrera[]>(`${this.apiUrl}/facultad/${facultadId}`, { params });
  }

  // Crear nueva carrera
  createCarrera(carrera: CarreraRequest): Observable<Carrera> {
    return this.http.post<Carrera>(this.apiUrl, carrera);
  }

  // Actualizar carrera
  updateCarrera(id: number, carrera: CarreraRequest): Observable<Carrera> {
    return this.http.put<Carrera>(`${this.apiUrl}/${id}`, carrera);
  }

  // Eliminar carrera
  deleteCarrera(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Buscar carrera por nombre
  buscarPorNombre(nombre: string): Observable<Carrera> {
    return this.http.get<Carrera>(`${this.apiUrl}/buscar/nombre/${nombre}`);
  }

  // Buscar carreras por duración
  buscarPorDuracion(duracion: number): Observable<Carrera[]> {
    return this.http.get<Carrera[]>(`${this.apiUrl}/buscar/duracion/${duracion}`);
  }
}