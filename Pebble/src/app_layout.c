#include <pebble.h>
#include "app_layout.h"

// BEGIN AUTO-GENERATED UI CODE; DO NOT MODIFY
static Window *s_window;
static GBitmap *s_res_logo_s;
static GFont s_res_gothic_28_bold;
static GFont s_res_gothic_18_bold;
static BitmapLayer *logo;
static TextLayer *name_text;
static TextLayer *stop_name_text;
static MenuLayer *stop_time_menu;

static void initialise_ui(void) {
  s_window = window_create();
  #ifndef PBL_SDK_3
    window_set_fullscreen(s_window, 0);
  #endif
  
  s_res_logo_s = gbitmap_create_with_resource(RESOURCE_ID_logo_s);
  s_res_gothic_28_bold = fonts_get_system_font(FONT_KEY_GOTHIC_28_BOLD);
  s_res_gothic_18_bold = fonts_get_system_font(FONT_KEY_GOTHIC_18_BOLD);
  // logo
  logo = bitmap_layer_create(GRect(95, 34, 47, 47));
  bitmap_layer_set_bitmap(logo, s_res_logo_s);
  layer_add_child(window_get_root_layer(s_window), (Layer *)logo);
  
  // name_text
  name_text = text_layer_create(GRect(-1, 0, 145, 28));
  text_layer_set_text(name_text, "TransitTimes");
  text_layer_set_text_alignment(name_text, GTextAlignmentCenter);
  text_layer_set_font(name_text, s_res_gothic_28_bold);
  layer_add_child(window_get_root_layer(s_window), (Layer *)name_text);
  
  // stop_name_text
  stop_name_text = text_layer_create(GRect(4, 31, 90, 50));
  text_layer_set_text(stop_name_text, "No Stop Info");
  text_layer_set_text_alignment(stop_name_text, GTextAlignmentCenter);
  text_layer_set_font(stop_name_text, s_res_gothic_18_bold);
  layer_add_child(window_get_root_layer(s_window), (Layer *)stop_name_text);
  
  // stop_time_menu
  stop_time_menu = menu_layer_create(GRect(-1, 87, 145, 65));
  menu_layer_set_click_config_onto_window(stop_time_menu, s_window);
  layer_add_child(window_get_root_layer(s_window), (Layer *)stop_time_menu);
}

static void destroy_ui(void) {
  window_destroy(s_window);
  bitmap_layer_destroy(logo);
  text_layer_destroy(name_text);
  text_layer_destroy(stop_name_text);
  menu_layer_destroy(stop_time_menu);
  gbitmap_destroy(s_res_logo_s);
}
// END AUTO-GENERATED UI CODE

static void handle_window_unload(Window* window) {
  destroy_ui();
}

void show_app_layout(void) {
  initialise_ui();
  window_set_window_handlers(s_window, (WindowHandlers) {
    .unload = handle_window_unload,
  });
  window_stack_push(s_window, true);
}

void hide_app_layout(void) {
  window_stack_remove(s_window, true);
}

void setMenuLayerCallback(MenuLayerCallbacks callbacks){
   menu_layer_set_callbacks(stop_time_menu,NULL,callbacks);
}

void setStopName(char* name){
  text_layer_set_overflow_mode(stop_name_text,GTextOverflowModeWordWrap);
  text_layer_set_text(stop_name_text, name);
}

void reloadMenu(){
  menu_layer_reload_data(stop_time_menu);
}

