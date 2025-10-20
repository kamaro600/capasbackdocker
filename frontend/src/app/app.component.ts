import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotificationsComponent } from './components/notifications/notifications.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, NotificationsComponent],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary-custom">
      <div class="container">
        <a class="navbar-brand" routerLink="/">
          <i class="material-icons me-2">school</i>
          Universidad
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" 
                data-bs-target="#navbarNav" aria-controls="navbarNav" 
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/" routerLinkActive="active" 
                 [routerLinkActiveOptions]="{exact: true}">
                <i class="material-icons me-1">home</i>
                Inicio
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/facultades" routerLinkActive="active">
                <i class="material-icons me-1">business</i>
                Facultades
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/carreras" routerLinkActive="active">
                <i class="material-icons me-1">book</i>
                Carreras
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container-fluid py-4">
      <router-outlet></router-outlet>
    </main>

    <app-notifications></app-notifications>

    <footer class="bg-light text-center py-3 mt-5">
      <div class="container">
        <p class="text-muted mb-0">
          &copy; 2024 Sistema de Gestión Universitaria. 
          Desarrollado con Angular y Spring Boot.
        </p>
      </div>
    </footer>
  `,
  styles: [`
    .navbar-brand {
      font-weight: 500;
      font-size: 1.5rem;
    }
    
    .nav-link {
      display: flex;
      align-items: center;
      transition: all 0.3s ease;
    }
    
    .nav-link:hover {
      background-color: rgba(255,255,255,0.1);
      border-radius: 4px;
    }
    
    .nav-link.active {
      background-color: rgba(255,255,255,0.2);
      border-radius: 4px;
    }
    
    main {
      min-height: calc(100vh - 200px);
    }
    
    footer {
      margin-top: auto;
    }
  `]
})
export class AppComponent {
  title = 'Universidad Frontend';
}