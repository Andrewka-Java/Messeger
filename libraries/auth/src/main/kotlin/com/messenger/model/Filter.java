package com.messenger.model;

class Request {}
class Response {}
interface HttpHandler {
    Response process(Request request);
}

interface Filter {
    HttpHandler process(HttpHandler next);
}
class Main {
    public static void main(String[] args) {
        final Filter filter = new Filter() {
            @Override
            public HttpHandler process(HttpHandler next) {
                return new HttpHandler() {
                    @Override
                    public Response process(Request request) {
                        return next.process(request);
                    }
                };
            }
        };
        final Filter filter1 = next -> new HttpHandler() {
            @Override
            public Response process(Request request) {
                return next.process(request);
            }
        };
        final Filter filter2 = next -> (HttpHandler) request -> next.process(request);
        final Filter filter3 = next -> (HttpHandler) next::process;
    }
}

