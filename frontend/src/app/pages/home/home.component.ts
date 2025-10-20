import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          
          <!-- Hero Section -->
          <div class="text-center mb-5 fade-in">
            <h1 class="display-4 text-primary-custom mb-3">
              <i class="material-icons me-3" style="font-size: 3rem; vertical-align: top;">school</i>
              Sistema de Gestión Universitaria
            </h1>
            <p class="lead text-muted">
              Plataforma integral para la administración de facultades y carreras universitarias
            </p>
          </div>

          <!-- Features Cards -->
          <div class="row g-4 mb-5">
            <div class="col-md-6">
              <div class="card card-custom h-100 slide-up">
                <div class="card-body text-center p-4">
                  <div class="mb-3">
                    <i class="material-icons text-primary-custom" style="font-size: 3rem;">business</i>
                  </div>
                  <h5 class="card-title">Gestión de Facultades</h5>
                  <p class="card-text text-muted mb-4">
                    Administra la información completa de las facultades: datos generales, 
                    ubicación, decanos y estado de actividad.
                  </p>
                  <a routerLink="/facultades" class="btn btn-primary-custom">
                    <i class="material-icons me-2">arrow_forward</i>
                    Ir a Facultades
                  </a>
                </div>
              </div>
            </div>
            
            <div class="col-md-6">
              <div class="card card-custom h-100 slide-up" style="animation-delay: 0.2s;">
                <div class="card-body text-center p-4">
                  <div class="mb-3">
                    <i class="material-icons text-primary-custom" style="font-size: 3rem;">book</i>
                  </div>
                  <h5 class="card-title">Gestión de Carreras</h5>
                  <p class="card-text text-muted mb-4">
                    Controla el catálogo de carreras universitarias: programas académicos, 
                    duración, títulos y vinculación con facultades.
                  </p>
                  <a routerLink="/carreras" class="btn btn-primary-custom">
                    <i class="material-icons me-2">arrow_forward</i>
                    Ir a Carreras
                  </a>
                </div>
              </div>
            </div>
          </div>

          <!-- Statistics Section -->
          <div class="row g-4 mb-5">
            <div class="col-12">
              <div class="card card-custom">
                <div class="card-header bg-primary-custom text-white">
                  <h5 class="mb-0">
                    <i class="material-icons me-2">analytics</i>
                    Resumen del Sistema
                  </h5>
                </div>
                <div class="card-body">
                  <div class="row text-center">
                    <div class="col-md-3">
                      <div class="p-3">
                        <i class="material-icons text-primary-custom mb-2" style="font-size: 2rem;">business</i>
                        <h4 class="text-primary-custom">{{stats.facultades}}</h4>
                        <p class="text-muted mb-0">Facultades Registradas</p>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="p-3">
                        <i class="material-icons text-success mb-2" style="font-size: 2rem;">check_circle</i>
                        <h4 class="text-success">{{stats.facultadesActivas}}</h4>
                        <p class="text-muted mb-0">Facultades Activas</p>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="p-3">
                        <i class="material-icons text-primary-custom mb-2" style="font-size: 2rem;">book</i>
                        <h4 class="text-primary-custom">{{stats.carreras}}</h4>
                        <p class="text-muted mb-0">Carreras Disponibles</p>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="p-3">
                        <i class="material-icons text-success mb-2" style="font-size: 2rem;">trending_up</i>
                        <h4 class="text-success">{{stats.carrerasActivas}}</h4>
                        <p class="text-muted mb-0">Carreras Activas</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Quick Actions -->
          <div class="row g-4">
            <div class="col-12">
              <div class="card card-custom">
                <div class="card-header">
                  <h5 class="mb-0">
                    <i class="material-icons me-2">flash_on</i>
                    Acciones Rápidas
                  </h5>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <a routerLink="/facultades" class="btn btn-outline-primary w-100 p-3">
                        <i class="material-icons me-2">add</i>
                        Nueva Facultad
                      </a>
                    </div>
                    <div class="col-md-6">
                      <a routerLink="/carreras" class="btn btn-outline-primary w-100 p-3">
                        <i class="material-icons me-2">add</i>
                        Nueva Carrera
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  `,
  styles: [`
    .slide-up {
      animation: slideUp 0.6s ease-out;
    }
    
    .card:hover {
      transform: translateY(-5px);
      transition: transform 0.3s ease;
    }
    
    .btn:hover {
      transform: translateY(-2px);
      transition: transform 0.3s ease;
    }
  `]
})
export class HomeComponent {
  stats = {
    facultades: 5,
    facultadesActivas: 5,
    carreras: 10,
    carrerasActivas: 10
  };

  constructor() {
    // Aquí podrías cargar estadísticas reales de la API
  }
}