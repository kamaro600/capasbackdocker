export interface Facultad {
  facultadId?: number;
  nombre: string;
  descripcion?: string;
  ubicacion?: string;
  decano?: string;
  fechaRegistro?: string;
  activo?: boolean;
}

export interface FacultadRequest {
  nombre: string;
  descripcion?: string;
  ubicacion?: string;
  decano?: string;
  activo?: boolean;
}