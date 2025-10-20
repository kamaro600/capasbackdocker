export interface Carrera {
  carreraId?: number;
  facultadId: number;
  nombre: string;
  descripcion?: string;
  duracionSemestres: number;
  tituloOtorgado?: string;
  fechaRegistro?: string;
  activo?: boolean;
  nombreFacultad?: string;
}

export interface CarreraRequest {
  facultadId: number;
  nombre: string;
  descripcion?: string;
  duracionSemestres: number;
  tituloOtorgado?: string;
  activo?: boolean;
}