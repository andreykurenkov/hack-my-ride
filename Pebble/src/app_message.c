#include <pebble.h>
#include <app_layout.h>
Window *window;	
	
// Key values for AppMessage Dictionary
enum {
	STOP_NAME_KEY = 0,	
	NUM_TIMES_KEY = 1
};

typedef struct _transit_info{
  char* stop_name;
  uint8_t num_times;
  char** route_names;
  char** stop_times;
} stop_info;

char** allocate(uint8_t num_elements,char** p){
  p = (char**)malloc(num_elements*sizeof(char*));
  return p;
}

stop_info* info;

// Called when a message is received from PebbleKitJS
static void in_received_handler(DictionaryIterator *received, void *context) {
	Tuple *tuple;
	uint8_t num_times,i;
	tuple = dict_find(received, STOP_NAME_KEY);
	if(tuple) {
		APP_LOG(APP_LOG_LEVEL_DEBUG, "Received info for stop %s", tuple->value->cstring); 
    return;
	}
  info = (stop_info*) malloc(sizeof(stop_info*)); 
	info->stop_name = tuple->value->cstring;
  setStopName(info->stop_name);
	
  tuple = dict_find(received, NUM_TIMES_KEY);
	if(!tuple) {
		APP_LOG(APP_LOG_LEVEL_ERROR, "Could not read NUM_TIMES_KEY");
    return;
  }

  num_times =  tuple->value->uint8;
  info->num_times = num_times;
  allocate(num_times,info->route_names);
  allocate(num_times,info->stop_times);
	APP_LOG(APP_LOG_LEVEL_DEBUG, "Received info for %d times", num_times);
            
  for(i=0;i<num_times;i++){
    tuple = dict_find(received, 2+2*i);
    if(!tuple) {
		  APP_LOG(APP_LOG_LEVEL_ERROR, "Could not read data at key %d",i);
      return;
    }
    info->route_names[i] = tuple->value->cstring;
      
    tuple = dict_find(received, 3+2*i);
    if(!tuple) {
		  APP_LOG(APP_LOG_LEVEL_ERROR, "Could not read data at key %d",i);
      return;
    }
    info->stop_times[i] = tuple->value->cstring;
  }
}

static uint16_t menu_get_num_sections_callback(MenuLayer *menu_layer, void *data) {
  return 1;
}

static uint16_t menu_get_num_rows_callback(MenuLayer *menu_layer, uint16_t section_index, void *data) {
  if(info==NULL)
    return 0;
  return info->num_times;
}

static int16_t menu_get_header_height_callback(MenuLayer *menu_layer, uint16_t section_index, void *data) {
  return MENU_CELL_BASIC_HEADER_HEIGHT;
}

static void menu_draw_header_callback(GContext* ctx, const Layer *cell_layer, uint16_t section_index, void *data) {
  menu_cell_basic_header_draw(ctx, cell_layer, "Stop Times");
}

static void menu_draw_row_callback(GContext* ctx, const Layer *cell_layer, MenuIndex *cell_index, void *data) {
  // This is a basic menu item with a title and subtitle
  menu_cell_basic_draw(ctx, cell_layer, info->route_names[cell_index->row], info->stop_times[cell_index->row], NULL);
}

static void menu_select_callback(MenuLayer *menu_layer, MenuIndex *cell_index, void *data) {
}

// Called when an incoming message from PebbleKitJS is dropped
static void in_dropped_handler(AppMessageResult reason, void *context) {	
}

// Called when PebbleKitJS does not acknowledge receipt of a message
static void out_failed_handler(DictionaryIterator *failed, AppMessageResult reason, void *context) {
}

void init(void) {
  show_app_layout();
	
	// Register AppMessage handlers
	app_message_register_inbox_received(in_received_handler); 
	app_message_register_inbox_dropped(in_dropped_handler); 
	app_message_register_outbox_failed(out_failed_handler);
		
	app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());
  
	setMenuLayerCallback((MenuLayerCallbacks){
    .get_num_sections = menu_get_num_sections_callback,
    .get_num_rows = menu_get_num_rows_callback,
    .get_header_height = menu_get_header_height_callback,
    .draw_header = menu_draw_header_callback,
    .draw_row = menu_draw_row_callback,
    .select_click = menu_select_callback,
  });
}

void deinit(void) {
	app_message_deregister_callbacks();
	window_destroy(window);
}

int main( void ) {
	init();
	app_event_loop();
	deinit();
}