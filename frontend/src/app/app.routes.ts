import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'facultades',
    loadComponent: () => import('./pages/facultades/facultades.component').then(m => m.FacultadesComponent)
  },
  {
    path: 'carreras',
    loadComponent: () => import('./pages/carreras/carreras.component').then(m => m.CarrerasComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];