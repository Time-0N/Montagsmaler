import {
  Component, ElementRef, OnInit, ViewChild, AfterViewInit
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { fabric } from 'fabric';
import { WebSocketService } from '../../../service/web-socket.service';
import { Store } from '@ngrx/store';
import { User } from '../../../api/models/user';
import { UserService } from '../../../api/services/user.service';
import { selectCurrentDrawer } from '../../../store/game-store/game-store.selectors';

@Component({
  selector: 'app-drawing-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './drawing-component.component.html',
  styleUrls: ['./drawing-component.component.scss']
})
export class DrawingComponentComponent implements OnInit, AfterViewInit {
  @ViewChild('canvas', { static: true }) canvasRef!: ElementRef<HTMLCanvasElement>;

  private canvas!: fabric.Canvas;
  private roomId!: string;
  private user: User | null = null;
  private canDraw = false;

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
        this.canDraw = drawer?.id === this.user?.id;
        if (this.canvas) {
          this.canvas.isDrawingMode = this.canDraw;
        }
      });

      this.wsService.drawingReceived$.subscribe(data => {
        if (data.senderSessionId !== this.user?.id && data.path) {
          fabric.util.enlivenObjects(
            [data.path] as any[],
            (objects: fabric.Object[]) => {
              objects.forEach((obj: fabric.Object) => this.canvas.add(obj));
              this.canvas.requestRenderAll();
            },
            ''
          );
        }
      });
    });
  }

  ngAfterViewInit(): void {
    this.canvas = new fabric.Canvas(this.canvasRef.nativeElement, {
      isDrawingMode: this.canDraw,
    });

    this.canvas.setHeight(600);
    this.canvas.setWidth(800);
    this.canvas.freeDrawingBrush.width = 3;
    this.canvas.freeDrawingBrush.color = 'black';

    this.canvas.on('path:created', (e: any) => {
      const path = e.path;
      const drawingData = {
        roomId: this.roomId,
        path: path.toObject(),
        senderSessionId: this.user?.id
      };
      this.wsService.sendDrawing(this.roomId, drawingData);
    });
  }
}
