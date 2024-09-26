function fn() {
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  return {
    baseUrl: 'http://localhost:' + karate.properties['karate.server.port']
  };
}