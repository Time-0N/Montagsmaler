import { TestBed } from '@angular/core/testing';
import { WebSocketService } from './web-socket.service';
import { Store } from '@ngrx/store';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { updateFromSocket } from '../store/game-store/game-store.actions';
import { GameSession } from '../generated';

describe('WebSocketService', () => {
  let service: WebSocketService;
  let storeSpy: jasmine.SpyObj<Store>;
  let mockClient: Partial<Client>;
  let subscriptions: StompSubscription[] = [];
  let subscribeCallbacks: Record<string, (msg: IMessage) => void> = {};

  beforeEach(() => {
    storeSpy = jasmine.createSpyObj('Store', ['dispatch']);
    subscribeCallbacks = {};
    subscriptions = [];

    mockClient = {
      connected: true,
      webSocket: { readyState: WebSocket.OPEN } as any,
      activate: jasmine.createSpy('activate').and.callFake(() => {
        if (mockClient.onConnect) {
          mockClient.onConnect({} as any);
        }
      }),
      deactivate: jasmine.createSpy('deactivate'),
      publish: jasmine.createSpy('publish'),
      subscribe: jasmine.createSpy('subscribe').and.callFake((topic: string, cb: any) => {
        subscribeCallbacks[topic] = cb;
        const sub = {
          id: Math.random().toString(),
          unsubscribe: jasmine.createSpy('unsubscribe')
        } as unknown as StompSubscription;
        subscriptions.push(sub);
        return sub;
      })
    };

    ;(mockClient as any)._transport = { connected: true };

    spyOn(WebSocketService.prototype as any, 'createClient').and.returnValue(mockClient);

    TestBed.configureTestingModule({
      providers: [
        WebSocketService,
        { provide: Store, useValue: storeSpy }
      ]
    });

    service = TestBed.inject(WebSocketService);
  });

  it('should create the service', () => {
    expect(service).toBeTruthy();
  });

  it('should connect and subscribe to topics', () => {
    service.connect('test-room');
    expect(mockClient.activate).toHaveBeenCalled();

    (mockClient.onConnect as any)({});

    const expectedTopics = [
      '/topic/game/test-room/state',
      '/topic/game/test-room/chat',
      '/topic/game/test-room/draw',
      '/user/queue/notifications'
    ];

    expectedTopics.forEach(topic => {
      expect(mockClient.subscribe).toHaveBeenCalledWith(
        topic,
        jasmine.any(Function)
      );
    });
  });

  it('should handle game state updates', () => {
    service.connect('test-room');
    const testState = { roomId: 'test-room' } as GameSession;
    const callback = subscribeCallbacks['/topic/game/test-room/state'];

    callback({ body: JSON.stringify(testState) } as IMessage);
    expect(storeSpy.dispatch).toHaveBeenCalledWith(updateFromSocket({ session: testState }));
  });

  it('should clean up resources on disconnect', () => {
    service.connect('test-room');
    service.disconnect();

    subscriptions.forEach(sub => {
      expect(sub.unsubscribe).toHaveBeenCalled();
    });
    expect(mockClient.deactivate).toHaveBeenCalled();
  });
});
