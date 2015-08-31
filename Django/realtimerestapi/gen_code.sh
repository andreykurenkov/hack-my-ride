export LD_LIBRARY_PATH=/usr/local/lib
protoc --python_out=. ./gtfs-realtime.proto
