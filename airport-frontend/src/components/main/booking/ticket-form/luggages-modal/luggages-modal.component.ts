import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-luggages-modal',
  imports: [
    MatDialogModule,
    MatDividerModule,
    MatRadioModule,
    CommonModule,
    FormsModule,
    MatOptionModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './luggages-modal.component.html',
  styleUrl: './luggages-modal.component.css'
})
export class LuggagesModalComponent {

  bagCount = 0;
  bagWeights: { value: number }[] = [];

  @Output() bagWeightsChange = new EventEmitter<{ value: number }[]>();

  constructor(
    private readonly dialogRef: MatDialogRef<LuggagesModalComponent>
  ) {
  }

  onBagCountChange(count: number): void {
    this.bagCount = count;

    this.bagWeights = Array.from(
      { length: count },
      (_, i) => ({ value: this.bagWeights[i]?.value ?? 20 })
    );
    this.emitChanges();
  }

  updateBagWeight(index: number, newWeight: number): void {
    this.bagWeights[index].value = newWeight;
    this.emitChanges();
  }

  private emitChanges(): void {
    this.bagWeightsChange.emit(this.bagWeights);
  }

  trackByIndex(index: number, item: any) {
    return index;
  }

  save(): void {
    this.dialogRef.close(this.bagWeights);
  }

  close(): void {
    this.dialogRef.close();
  }
}
