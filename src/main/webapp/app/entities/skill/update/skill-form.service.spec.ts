import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../skill.test-samples';

import { SkillFormService } from './skill-form.service';

describe('Skill Form Service', () => {
  let service: SkillFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillFormService);
  });

  describe('Service methods', () => {
    describe('createSkillFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSkillFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            perfilUsuarios: expect.any(Object),
          }),
        );
      });

      it('passing ISkill should create a new form with FormGroup', () => {
        const formGroup = service.createSkillFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            perfilUsuarios: expect.any(Object),
          }),
        );
      });
    });

    describe('getSkill', () => {
      it('should return NewSkill for default Skill initial value', () => {
        const formGroup = service.createSkillFormGroup(sampleWithNewData);

        const skill = service.getSkill(formGroup) as any;

        expect(skill).toMatchObject(sampleWithNewData);
      });

      it('should return NewSkill for empty Skill initial value', () => {
        const formGroup = service.createSkillFormGroup();

        const skill = service.getSkill(formGroup) as any;

        expect(skill).toMatchObject({});
      });

      it('should return ISkill', () => {
        const formGroup = service.createSkillFormGroup(sampleWithRequiredData);

        const skill = service.getSkill(formGroup) as any;

        expect(skill).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISkill should not enable id FormControl', () => {
        const formGroup = service.createSkillFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSkill should disable id FormControl', () => {
        const formGroup = service.createSkillFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
