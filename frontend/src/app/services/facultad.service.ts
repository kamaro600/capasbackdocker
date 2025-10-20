import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Facultad, FacultadRequest } from '../models/facultad.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FacultadService {
  private readonly apiUrl = `${environment.apiUrl}/facultades`;

  constructor(private http: HttpClient) {}

  // Obtener todas las facultades
  getFacultades(soloActivas = false): Observable<Facultad[]> {
    const params = new HttpParams().set('soloActivas', soloActivas.toString());
    return this.http.get<Facultad[]>(this.apiUrl, { params });
  }

  // Obtener facultad por ID
  getFacultadById(id: number): Observable<Facultad> {
    return this.http.get<Facultad>(`${this.apiUrl}/${id}`);
  }

  // Crear nueva facultad
  createFacultad(facultad: FacultadRequest): Observable<Facultad> {
    return this.http.post<Facultad>(this.apiUrl, facultad);
  }

  // Actualizar facultad
  updateFacultad(id: number, facultad: FacultadRequest): Observable<Facultad> {
    return this.http.put<Facultad>(`${this.apiUrl}/${id}`, facultad);
  }

  // Eliminar facultad
  deleteFacultad(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Buscar facultad por nombre
  buscarPorNombre(nombre: string): Observable<Facultad> {
    return this.http.get<Facultad>(`${this.apiUrl}/buscar/nombre/${nombre}`);
  }

  // Buscar facultades por decano
  buscarPorDecano(decano: string): Observable<Facultad[]> {
    const params = new HttpParams().set('decano', decano);
    return this.http.get<Facultad[]>(`${this.apiUrl}/buscar/decano`, { params });
  }
}