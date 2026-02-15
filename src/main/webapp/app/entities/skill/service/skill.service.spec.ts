import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISkill } from '../skill.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../skill.test-samples';

import { SkillService } from './skill.service';

const requireRestSample: ISkill = {
  ...sampleWithRequiredData,
};

describe('Skill Service', () => {
  let service: SkillService;
  let httpMock: HttpTestingController;
  let expectedResult: ISkill | ISkill[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SkillService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Skill', () => {
      const skill = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(skill).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Skill', () => {
      const skill = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(skill).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Skill', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Skill', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Skill', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSkillToCollectionIfMissing', () => {
      it('should add a Skill to an empty array', () => {
        const skill: ISkill = sampleWithRequiredData;
        expectedResult = service.addSkillToCollectionIfMissing([], skill);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skill);
      });

      it('should not add a Skill to an array that contains it', () => {
        const skill: ISkill = sampleWithRequiredData;
        const skillCollection: ISkill[] = [
          {
            ...skill,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, skill);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Skill to an array that doesn't contain it", () => {
        const skill: ISkill = sampleWithRequiredData;
        const skillCollection: ISkill[] = [sampleWithPartialData];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, skill);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skill);
      });

      it('should add only unique Skill to an array', () => {
        const skillArray: ISkill[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const skillCollection: ISkill[] = [sampleWithRequiredData];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, ...skillArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const skill: ISkill = sampleWithRequiredData;
        const skill2: ISkill = sampleWithPartialData;
        expectedResult = service.addSkillToCollectionIfMissing([], skill, skill2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skill);
        expect(expectedResult).toContain(skill2);
      });

      it('should accept null and undefined values', () => {
        const skill: ISkill = sampleWithRequiredData;
        expectedResult = service.addSkillToCollectionIfMissing([], null, skill, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skill);
      });

      it('should return initial array if no Skill is added', () => {
        const skillCollection: ISkill[] = [sampleWithRequiredData];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, undefined, null);
        expect(expectedResult).toEqual(skillCollection);
      });
    });

    describe('compareSkill', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSkill(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24455 };
        const entity2 = null;

        const compareResult1 = service.compareSkill(entity1, entity2);
        const compareResult2 = service.compareSkill(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24455 };
        const entity2 = { id: 21768 };

        const compareResult1 = service.compareSkill(entity1, entity2);
        const compareResult2 = service.compareSkill(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24455 };
        const entity2 = { id: 24455 };

        const compareResult1 = service.compareSkill(entity1, entity2);
        const compareResult2 = service.compareSkill(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
