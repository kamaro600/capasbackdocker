import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { FacultadService } from '../../services/facultad.service';
import { Facultad } from '../../models/facultad.model';
import { FacultadRequest } from '../../models/facultad-request.model';

@Component({
  selector: 'app-facultades',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          
          <!-- Header -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
              <h2 class="text-primary-custom mb-1">
                <i class="material-icons me-2">business</i>
                Gestión de Facultades
              </h2>
              <p class="text-muted mb-0">Administra las facultades de la universidad</p>
            </div>
            <button class="btn btn-primary-custom" (click)="openModal('create')" [disabled]="loading">
              <i class="material-icons me-2">add</i>
              Nueva Facultad
            </button>
          </div>

          <!-- Filters -->
          <div class="card card-custom mb-4">
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-4">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="searchInput"
                      [(ngModel)]="searchTerm"
                      (input)="onSearch()"
                      placeholder="Buscar facultad...">
                    <label for="searchInput">Buscar por nombre</label>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-floating">
                    <select 
                      class="form-select" 
                      id="statusFilter"
                      [(ngModel)]="statusFilter"
                      (change)="onFilterChange()">
                      <option value="all">Todas</option>
                      <option value="active">Solo Activas</option>
                      <option value="inactive">Solo Inactivas</option>
                    </select>
                    <label for="statusFilter">Estado</label>
                  </div>
                </div>
                <div class="col-md-3">
                  <button class="btn btn-outline-secondary h-100 w-100" (click)="loadFacultades()" [disabled]="loading">
                    <i class="material-icons me-2">refresh</i>
                    Actualizar
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Loading -->
          <div *ngIf="loading" class="text-center py-5">
            <div class="spinner-custom mx-auto"></div>
            <p class="mt-3 text-muted">Cargando facultades...</p>
          </div>

          <!-- Error Message -->
          <div *ngIf="error" class="alert alert-danger" role="alert">
            <i class="material-icons me-2">error</i>
            {{error}}
          </div>

          <!-- Facultades Table -->
          <div *ngIf="!loading" class="card card-custom">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="material-icons me-2">list</i>
                Lista de Facultades ({{filteredFacultades.length}})
              </h5>
            </div>
            <div class="card-body p-0">
              <div class="table-responsive">
                <table class="table table-custom table-hover mb-0">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Decano</th>
                      <th>Ubicación</th>
                      <th>Estado</th>
                      <th>Fecha Registro</th>
                      <th width="150">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let facultad of filteredFacultades" class="fade-in">
                      <td>
                        <div>
                          <strong>{{facultad.nombre}}</strong>
                          <div *ngIf="facultad.descripcion" class="text-muted small">
                            {{facultad.descripcion | slice:0:50}}{{facultad.descripcion && facultad.descripcion.length > 50 ? '...' : ''}}
                          </div>
                        </div>
                      </td>
                      <td>{{facultad.decano || 'No asignado'}}</td>
                      <td>{{facultad.ubicacion || 'No especificada'}}</td>
                      <td>
                        <span class="badge" [class]="facultad.activo ? 'badge-active' : 'badge-inactive'">
                          {{facultad.activo ? 'Activa' : 'Inactiva'}}
                        </span>
                      </td>
                      <td>{{facultad.fechaRegistro | date:'dd/MM/yyyy HH:mm'}}</td>
                      <td>
                        <div class="btn-group btn-group-sm" role="group">
                          <button 
                            class="btn btn-outline-primary" 
                            (click)="openModal('edit', facultad)"
                            title="Editar">
                            <i class="material-icons">edit</i>
                          </button>
                          <button 
                            class="btn btn-outline-danger" 
                            (click)="deleteFacultad(facultad)"
                            [disabled]="facultad.activo === false"
                            title="Eliminar">
                            <i class="material-icons">delete</i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <tr *ngIf="filteredFacultades.length === 0 && !loading">
                      <td colspan="6" class="text-center py-4 text-muted">
                        <i class="material-icons mb-2" style="font-size: 3rem;">search_off</i>
                        <div>No se encontraron facultades</div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" [class.show]="showModal" [style.display]="showModal ? 'block' : 'none'" 
         tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="material-icons me-2">{{modalMode === 'create' ? 'add' : 'edit'}}</i>
              {{modalMode === 'create' ? 'Nueva Facultad' : 'Editar Facultad'}}
            </h5>
            <button type="button" class="btn-close" (click)="closeModal()"></button>
          </div>
          <form [formGroup]="facultadForm" (ngSubmit)="onSubmit()">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="nombre"
                      formControlName="nombre"
                      placeholder="Nombre de la facultad"
                      [class.is-invalid]="facultadForm.get('nombre')?.invalid && facultadForm.get('nombre')?.touched">
                    <label for="nombre">Nombre *</label>
                    <div class="invalid-feedback" *ngIf="facultadForm.get('nombre')?.invalid && facultadForm.get('nombre')?.touched">
                      El nombre es obligatorio y debe tener máximo 100 caracteres
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="decano"
                      formControlName="decano"
                      placeholder="Nombre del decano">
                    <label for="decano">Decano</label>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="ubicacion"
                      formControlName="ubicacion"
                      placeholder="Ubicación de la facultad">
                    <label for="ubicacion">Ubicación</label>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-check form-switch mt-3">
                    <input 
                      class="form-check-input" 
                      type="checkbox" 
                      id="activo"
                      formControlName="activo">
                    <label class="form-check-label" for="activo">
                      Facultad Activa
                    </label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating">
                    <textarea 
                      class="form-control" 
                      id="descripcion"
                      formControlName="descripcion"
                      placeholder="Descripción de la facultad"
                      style="height: 100px"></textarea>
                    <label for="descripcion">Descripción</label>
                  </div>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeModal()">
                <i class="material-icons me-2">close</i>
                Cancelar
              </button>
              <button type="submit" class="btn btn-primary-custom" [disabled]="facultadForm.invalid || submitting">
                <i class="material-icons me-2" *ngIf="!submitting">save</i>
                <div class="spinner-border spinner-border-sm me-2" *ngIf="submitting"></div>
                {{submitting ? 'Guardando...' : (modalMode === 'create' ? 'Crear' : 'Actualizar')}}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal Backdrop -->
    <div class="modal-backdrop fade" [class.show]="showModal" *ngIf="showModal" (click)="closeModal()"></div>
  `,
  styles: [`
    .modal {
      background-color: rgba(0,0,0,0.5);
    }
    
    .btn-group-sm .btn {
      padding: 0.25rem 0.5rem;
    }
    
    .table td {
      vertical-align: middle;
    }
    
    .form-floating > .form-control:focus ~ label {
      color: var(--primary-color);
    }
  `]
})
export class FacultadesComponent implements OnInit {
  facultades: Facultad[] = [];
  filteredFacultades: Facultad[] = [];
  searchTerm = '';
  statusFilter = 'all';
  loading = false;
  submitting = false;
  error = '';
  
  // Modal
  showModal = false;
  modalMode: 'create' | 'edit' = 'create';
  editingFacultad: Facultad | null = null;
  
  // Form
  facultadForm: FormGroup;

  constructor(
    private facultadService: FacultadService,
    private fb: FormBuilder
  ) {
    this.facultadForm = this.createForm();
  }

  ngOnInit() {
    this.loadFacultades();
  }

  createForm(): FormGroup {
    return this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: [''],
      ubicacion: ['', Validators.maxLength(100)],
      decano: ['', Validators.maxLength(100)],
      activo: [true]
    });
  }

  loadFacultades() {
    this.loading = true;
    this.error = '';
    
    this.facultadService.getFacultades().subscribe({
      next: (facultades) => {
        this.facultades = facultades;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar las facultades: ' + (err.error?.message || err.message);
        this.loading = false;
      }
    });
  }

  onSearch() {
    this.applyFilters();
  }

  onFilterChange() {
    this.applyFilters();
  }

  applyFilters() {
    let filtered = [...this.facultades];

    // Filtro por texto
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(f => 
        f.nombre.toLowerCase().includes(term) ||
        f.decano?.toLowerCase().includes(term) ||
        f.ubicacion?.toLowerCase().includes(term)
      );
    }

    // Filtro por estado
    if (this.statusFilter === 'active') {
      filtered = filtered.filter(f => f.activo === true);
    } else if (this.statusFilter === 'inactive') {
      filtered = filtered.filter(f => f.activo === false);
    }

    this.filteredFacultades = filtered;
  }

  openModal(mode: 'create' | 'edit', facultad?: Facultad) {
    this.modalMode = mode;
    this.editingFacultad = facultad || null;
    
    if (mode === 'edit' && facultad) {
      this.facultadForm.patchValue({
        nombre: facultad.nombre,
        descripcion: facultad.descripcion || '',
        ubicacion: facultad.ubicacion || '',
        decano: facultad.decano || '',
        activo: facultad.activo
      });
    } else {
      this.facultadForm.reset({
        nombre: '',
        descripcion: '',
        ubicacion: '',
        decano: '',
        activo: true
      });
    }
    
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.editingFacultad = null;
    this.facultadForm.reset();
  }

  onSubmit() {
    if (this.facultadForm.invalid) return;

    this.submitting = true;
    const formData: FacultadRequest = this.facultadForm.value;

    const operation = this.modalMode === 'create' 
      ? this.facultadService.createFacultad(formData)
      : this.facultadService.updateFacultad(this.editingFacultad!.facultadId!, formData);

    operation.subscribe({
      next: () => {
        this.submitting = false;
        this.closeModal();
        this.loadFacultades();
        // Aquí podrías mostrar un mensaje de éxito
      },
      error: (err) => {
        this.submitting = false;
        this.error = 'Error al guardar: ' + (err.error?.message || err.message);
      }
    });
  }

  deleteFacultad(facultad: Facultad) {
    if (confirm(`¿Estás seguro de que deseas eliminar la facultad "${facultad.nombre}"?`)) {
      this.facultadService.deleteFacultad(facultad.facultadId!).subscribe({
        next: () => {
          this.loadFacultades();
          // Aquí podrías mostrar un mensaje de éxito
        },
        error: (err) => {
          this.error = 'Error al eliminar: ' + (err.error?.message || err.message);
        }
      });
    }
  }
}