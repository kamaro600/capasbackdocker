import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { CarreraService } from '../../services/carrera.service';
import { FacultadService } from '../../services/facultad.service';
import { Carrera } from '../../models/carrera.model';
import { Facultad } from '../../models/facultad.model';
import { CarreraRequest } from '../../models/carrera-request.model';

@Component({
  selector: 'app-carreras',
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
                <i class="material-icons me-2">book</i>
                Gestión de Carreras
              </h2>
              <p class="text-muted mb-0">Administra las carreras universitarias</p>
            </div>
            <button class="btn btn-primary-custom" (click)="openModal('create')" [disabled]="loading">
              <i class="material-icons me-2">add</i>
              Nueva Carrera
            </button>
          </div>

          <!-- Filters -->
          <div class="card card-custom mb-4">
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-3">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="searchInput"
                      [(ngModel)]="searchTerm"
                      (input)="onSearch()"
                      placeholder="Buscar carrera...">
                    <label for="searchInput">Buscar por nombre</label>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-floating">
                    <select 
                      class="form-select" 
                      id="facultadFilter"
                      [(ngModel)]="facultadFilter"
                      (change)="onFilterChange()">
                      <option value="">Todas las facultades</option>
                      <option *ngFor="let facultad of facultades" [value]="facultad.facultadId">
                        {{facultad.nombre}}
                      </option>
                    </select>
                    <label for="facultadFilter">Facultad</label>
                  </div>
                </div>
                <div class="col-md-2">
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
                <div class="col-md-2">
                  <div class="form-floating">
                    <input 
                      type="number" 
                      class="form-control" 
                      id="duracionFilter"
                      [(ngModel)]="duracionFilter"
                      (input)="onFilterChange()"
                      placeholder="Semestres"
                      min="1"
                      max="20">
                    <label for="duracionFilter">Duración</label>
                  </div>
                </div>
                <div class="col-md-2">
                  <button class="btn btn-outline-secondary h-100 w-100" (click)="loadCarreras()" [disabled]="loading">
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
            <p class="mt-3 text-muted">Cargando carreras...</p>
          </div>

          <!-- Error Message -->
          <div *ngIf="error" class="alert alert-danger" role="alert">
            <i class="material-icons me-2">error</i>
            {{error}}
          </div>

          <!-- Carreras Table -->
          <div *ngIf="!loading" class="card card-custom">
            <div class="card-header">
              <h5 class="mb-0">
                <i class="material-icons me-2">list</i>
                Lista de Carreras ({{filteredCarreras.length}})
              </h5>
            </div>
            <div class="card-body p-0">
              <div class="table-responsive">
                <table class="table table-custom table-hover mb-0">
                  <thead>
                    <tr>
                      <th>Carrera</th>
                      <th>Facultad</th>
                      <th>Duración</th>
                      <th>Título</th>
                      <th>Estado</th>
                      <th>Fecha Registro</th>
                      <th width="150">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let carrera of filteredCarreras" class="fade-in">
                      <td>
                        <div>
                          <strong>{{carrera.nombre}}</strong>
                          <div *ngIf="carrera.descripcion" class="text-muted small">
                            {{carrera.descripcion | slice:0:50}}{{carrera.descripcion && carrera.descripcion.length > 50 ? '...' : ''}}
                          </div>
                        </div>
                      </td>
                      <td>
                        <span class="badge bg-light text-dark">{{carrera.nombreFacultad}}</span>
                      </td>
                      <td>
                        <span class="badge bg-info text-white">
                          {{carrera.duracionSemestres}} sem.
                        </span>
                      </td>
                      <td>{{carrera.tituloOtorgado || 'No especificado'}}</td>
                      <td>
                        <span class="badge" [class]="carrera.activo ? 'badge-active' : 'badge-inactive'">
                          {{carrera.activo ? 'Activa' : 'Inactiva'}}
                        </span>
                      </td>
                      <td>{{carrera.fechaRegistro | date:'dd/MM/yyyy HH:mm'}}</td>
                      <td>
                        <div class="btn-group btn-group-sm" role="group">
                          <button 
                            class="btn btn-outline-primary" 
                            (click)="openModal('edit', carrera)"
                            title="Editar">
                            <i class="material-icons">edit</i>
                          </button>
                          <button 
                            class="btn btn-outline-danger" 
                            (click)="deleteCarrera(carrera)"
                            [disabled]="carrera.activo === false"
                            title="Eliminar">
                            <i class="material-icons">delete</i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <tr *ngIf="filteredCarreras.length === 0 && !loading">
                      <td colspan="7" class="text-center py-4 text-muted">
                        <i class="material-icons mb-2" style="font-size: 3rem;">search_off</i>
                        <div>No se encontraron carreras</div>
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
              {{modalMode === 'create' ? 'Nueva Carrera' : 'Editar Carrera'}}
            </h5>
            <button type="button" class="btn-close" (click)="closeModal()" aria-label="Cerrar modal"></button>
          </div>
          <form [formGroup]="carreraForm" (ngSubmit)="onSubmit()">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="nombre"
                      formControlName="nombre"
                      placeholder="Nombre de la carrera"
                      [class.is-invalid]="carreraForm.get('nombre')?.invalid && carreraForm.get('nombre')?.touched">
                    <label for="nombre">Nombre *</label>
                    <div class="invalid-feedback" *ngIf="carreraForm.get('nombre')?.invalid && carreraForm.get('nombre')?.touched">
                      El nombre es obligatorio y debe tener máximo 100 caracteres
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-floating">
                    <select 
                      class="form-select" 
                      id="facultadId"
                      formControlName="facultadId"
                      [class.is-invalid]="carreraForm.get('facultadId')?.invalid && carreraForm.get('facultadId')?.touched">
                      <option value="">Seleccionar facultad</option>
                      <option *ngFor="let facultad of facultadesActivas" [value]="facultad.facultadId">
                        {{facultad.nombre}}
                      </option>
                    </select>
                    <label for="facultadId">Facultad *</label>
                    <div class="invalid-feedback" *ngIf="carreraForm.get('facultadId')?.invalid && carreraForm.get('facultadId')?.touched">
                      Debe seleccionar una facultad
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="number" 
                      class="form-control" 
                      id="duracionSemestres"
                      formControlName="duracionSemestres"
                      placeholder="Duración en semestres"
                      min="1"
                      max="20"
                      [class.is-invalid]="carreraForm.get('duracionSemestres')?.invalid && carreraForm.get('duracionSemestres')?.touched">
                    <label for="duracionSemestres">Duración (semestres) *</label>
                    <div class="invalid-feedback" *ngIf="carreraForm.get('duracionSemestres')?.invalid && carreraForm.get('duracionSemestres')?.touched">
                      La duración debe estar entre 1 y 20 semestres
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-floating">
                    <input 
                      type="text" 
                      class="form-control" 
                      id="tituloOtorgado"
                      formControlName="tituloOtorgado"
                      placeholder="Título que se otorga">
                    <label for="tituloOtorgado">Título Otorgado</label>
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
                      Carrera Activa
                    </label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating">
                    <textarea 
                      class="form-control" 
                      id="descripcion"
                      formControlName="descripcion"
                      placeholder="Descripción de la carrera"
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
              <button type="submit" class="btn btn-primary-custom" [disabled]="carreraForm.invalid || submitting">
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
    <div class="modal-backdrop fade" [class.show]="showModal" *ngIf="showModal" 
         (click)="closeModal()" 
         (keydown.escape)="closeModal()"
         role="button" 
         tabindex="0"
         aria-label="Cerrar modal"></div>
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
    
    .badge {
      font-size: 0.75em;
    }
  `]
})
export class CarrerasComponent implements OnInit {
  carreras: Carrera[] = [];
  filteredCarreras: Carrera[] = [];
  facultades: Facultad[] = [];
  facultadesActivas: Facultad[] = [];
  
  searchTerm = '';
  statusFilter = 'all';
  facultadFilter = '';
  duracionFilter: number | null = null;
  
  loading = false;
  submitting = false;
  error = '';
  
  // Modal
  showModal = false;
  modalMode: 'create' | 'edit' = 'create';
  editingCarrera: Carrera | null = null;
  
  // Form
  carreraForm: FormGroup;

  constructor(
    private carreraService: CarreraService,
    private facultadService: FacultadService,
    private fb: FormBuilder
  ) {
    this.carreraForm = this.createForm();
  }

  ngOnInit() {
    this.loadFacultades();
    this.loadCarreras();
  }

  createForm(): FormGroup {
    return this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: [''],
      facultadId: ['', Validators.required],
      duracionSemestres: ['', [Validators.required, Validators.min(1), Validators.max(20)]],
      tituloOtorgado: ['', Validators.maxLength(100)],
      activo: [true]
    });
  }

  loadFacultades() {
    this.facultadService.getFacultades().subscribe({
      next: (facultades) => {
        this.facultades = facultades;
        this.facultadesActivas = facultades.filter(f => f.activo);
      },
      error: (err) => {
        console.error('Error al cargar facultades:', err);
      }
    });
  }

  loadCarreras() {
    this.loading = true;
    this.error = '';
    
    this.carreraService.getCarreras().subscribe({
      next: (carreras) => {
        this.carreras = carreras;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar las carreras: ' + (err.error?.message || err.message);
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
    let filtered = [...this.carreras];

    // Filtro por texto
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(c => 
        c.nombre.toLowerCase().includes(term) ||
        c.nombreFacultad?.toLowerCase().includes(term) ||
        c.tituloOtorgado?.toLowerCase().includes(term)
      );
    }

    // Filtro por facultad
    if (this.facultadFilter) {
      filtered = filtered.filter(c => c.facultadId.toString() === this.facultadFilter);
    }

    // Filtro por estado
    if (this.statusFilter === 'active') {
      filtered = filtered.filter(c => c.activo === true);
    } else if (this.statusFilter === 'inactive') {
      filtered = filtered.filter(c => c.activo === false);
    }

    // Filtro por duración
    if (this.duracionFilter) {
      filtered = filtered.filter(c => c.duracionSemestres === this.duracionFilter);
    }

    this.filteredCarreras = filtered;
  }

  openModal(mode: 'create' | 'edit', carrera?: Carrera) {
    this.modalMode = mode;
    this.editingCarrera = carrera || null;
    
    if (mode === 'edit' && carrera) {
      this.carreraForm.patchValue({
        nombre: carrera.nombre,
        descripcion: carrera.descripcion || '',
        facultadId: carrera.facultadId,
        duracionSemestres: carrera.duracionSemestres,
        tituloOtorgado: carrera.tituloOtorgado || '',
        activo: carrera.activo
      });
    } else {
      this.carreraForm.reset({
        nombre: '',
        descripcion: '',
        facultadId: '',
        duracionSemestres: '',
        tituloOtorgado: '',
        activo: true
      });
    }
    
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.editingCarrera = null;
    this.carreraForm.reset();
  }

  onSubmit() {
    if (this.carreraForm.invalid) return;

    this.submitting = true;
    const formData: CarreraRequest = this.carreraForm.value;

    const operation = this.modalMode === 'create' 
      ? this.carreraService.createCarrera(formData)
      : this.carreraService.updateCarrera(this.editingCarrera!.carreraId!, formData);

    operation.subscribe({
      next: () => {
        this.submitting = false;
        this.closeModal();
        this.loadCarreras();
        // Aquí podrías mostrar un mensaje de éxito
      },
      error: (err) => {
        this.submitting = false;
        this.error = 'Error al guardar: ' + (err.error?.message || err.message);
      }
    });
  }

  deleteCarrera(carrera: Carrera) {
    if (confirm(`¿Estás seguro de que deseas eliminar la carrera "${carrera.nombre}"?`)) {
      this.carreraService.deleteCarrera(carrera.carreraId!).subscribe({
        next: () => {
          this.loadCarreras();
          // Aquí podrías mostrar un mensaje de éxito
        },
        error: (err) => {
          this.error = 'Error al eliminar: ' + (err.error?.message || err.message);
        }
      });
    }
  }
}