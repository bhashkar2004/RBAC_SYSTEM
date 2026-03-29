import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessLogs } from './access-logs';

describe('AccessLogs', () => {
  let component: AccessLogs;
  let fixture: ComponentFixture<AccessLogs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccessLogs],
    }).compileComponents();

    fixture = TestBed.createComponent(AccessLogs);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
