import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ProyectoService } from '../service/proyecto.service';
import { IProyecto } from '../proyecto.model';
import { ProyectoFormService } from './proyecto-form.service';

import { ProyectoUpdateComponent } from './proyecto-update.component';

describe('Proyecto Management Update Component', () => {
  let comp: ProyectoUpdateComponent;
  let fixture: ComponentFixture<ProyectoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proyectoFormService: ProyectoFormService;
  let proyectoService: ProyectoService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProyectoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProyectoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProyectoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proyectoFormService = TestBed.inject(ProyectoFormService);
    proyectoService = TestBed.inject(ProyectoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const proyecto: IProyecto = { id: 11127 };
      const autor: IUser = { id: 3944 };
      proyecto.autor = autor;
      const colaboradores: IUser[] = [{ id: 3944 }];
      proyecto.colaboradores = colaboradores;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [autor, ...colaboradores];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proyecto });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const proyecto: IProyecto = { id: 11127 };
      const autor: IUser = { id: 3944 };
      proyecto.autor = autor;
      const colaboradores: IUser = { id: 3944 };
      proyecto.colaboradores = [colaboradores];

      activatedRoute.data = of({ proyecto });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(autor);
      expect(comp.usersSharedCollection).toContainEqual(colaboradores);
      expect(comp.proyecto).toEqual(proyecto);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProyecto>>();
      const proyecto = { id: 10716 };
      jest.spyOn(proyectoFormService, 'getProyecto').mockReturnValue(proyecto);
      jest.spyOn(proyectoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proyecto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proyecto }));
      saveSubject.complete();

      // THEN
      expect(proyectoFormService.getProyecto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proyectoService.update).toHaveBeenCalledWith(expect.objectContaining(proyecto));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProyecto>>();
      const proyecto = { id: 10716 };
      jest.spyOn(proyectoFormService, 'getProyecto').mockReturnValue({ id: null });
      jest.spyOn(proyectoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proyecto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proyecto }));
      saveSubject.complete();

      // THEN
      expect(proyectoFormService.getProyecto).toHaveBeenCalled();
      expect(proyectoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProyecto>>();
      const proyecto = { id: 10716 };
      jest.spyOn(proyectoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proyecto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proyectoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
