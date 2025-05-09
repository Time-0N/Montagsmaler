import {
  Component, ElementRef, OnInit, ViewChild, AfterViewInit
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { WebSocketService } from '../../../service/web-socket.service';
import { selectCurrentDrawer } from '../../../store/game-store/game-store.selectors';
import { Store } from '@ngrx/store';
import { User } from '../../../api/models/user';
import {UserService} from '../../../api/services/user.service';

@Component({
  selector: 'app-drawing-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './drawing-component.component.html',
  styleUrls: ['./drawing-component.component.scss']
})
export class DrawingComponentComponent implements OnInit, AfterViewInit {
  @ViewChild('canvas', { static: true }) canvasRef!: ElementRef<HTMLCanvasElement>;

  private ctx!: CanvasRenderingContext2D;
  private isDrawing = false;
  private roomId!: string;
  private user: User | null = null;

  constructor(
    private wsService: WebSocketService,
    private store: Store,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.roomId = window.location.pathname.split('/').pop()!;

    this.userService.getCurrentUser().subscribe(user => {
      this.user = user;

      this.store.select(selectCurrentDrawer).subscribe(drawer => {
        this.isDrawing = drawer?.id === this.user?.id;
      });

      this.wsService.drawingReceived$.subscribe(data => {
        if (data.senderSessionId !== this.user?.id) {
          this.drawFromRemote(data);
        }
      })
    });
  }

  ngAfterViewInit(): void {
    const canvas = this.canvasRef.nativeElement;
    this.ctx = canvas.getContext('2d')!;
    canvas.width = 800;
    canvas.height = 600;

    canvas.addEventListener('mousedown', this.startDraw);
    canvas.addEventListener('mouseup', this.endDraw);
    canvas.addEventListener('mousemove', this.draw);
  }

  private startDraw = (event: MouseEvent) => {
    if (!this.isDrawing) return;
    this.ctx.beginPath();
    this.ctx.moveTo(event.offsetX, event.offsetY);
  };

  private endDraw = () => {
    if (!this.isDrawing) return;
    this.ctx.closePath();
  };

  private draw = (event: MouseEvent) => {
    if (!this.isDrawing) return;

    this.ctx.lineTo(event.offsetX, event.offsetY);
    this.ctx.stroke();

    const drawingData = {
      roomId: this.roomId,
      x: event.offsetX,
      y: event.offsetY,
      senderSessionId: this.user?.id
    };

    this.wsService.sendDrawing(this.roomId, drawingData);
  };

  private drawFromRemote(data: { x: number; y: number }): void {
    if (!this.ctx) return;

    this.ctx.lineTo(data.x, data.y);
    this.ctx.stroke();
  }
}
