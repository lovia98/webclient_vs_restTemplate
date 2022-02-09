# webclient_vs_restTemplate

## webclient 와 restTemplate 응답 속도 비교에 관한 간단한 샘플 코드

https://github.com/lovia98/webclient_vs_restTemplate/blob/main/src/main/java/com/example/reactive/sample/WebController.java

## 2초가 걸리는 API를 restTemplate 과 webclient 논블럭킹을 이용해 호출하는 결과 차이 테스트
```
    /**
     * restTemplate 으로 호출 (blocking)
     *
     * @return
     */
    @GetMapping("/tweets-blocking")
    public List<Tweet> getTweetsBlocking() {

        log.info("블럭킹 호출 시작");
        long start = System.currentTimeMillis();

        final String uri = getSlowServiceUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Tweet>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Tweet>>(){});

        List<Tweet> result = response.getBody();
        result.forEach(tweet -> log.info(tweet.toString()));

        log.info("블럭킹 호출 완료 - 걸린시간 : {}ms", System.currentTimeMillis() - start);
        return result;
    }
```

### 결과
```
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : 블럭킹 호출 시작
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : com.example.reactive.sample.Tweet@61a4ac68
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : com.example.reactive.sample.Tweet@7bd791ac
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : com.example.reactive.sample.Tweet@5f7a8bec
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : 블럭킹 호출 완료 - 걸린시간 : 2072ms

```



```
    /**
     * webclient 호출 (non-blockin)
     *
     * @return
     */
    @GetMapping(value = "/tweets-non-blocking", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> getTweetsNonBlocking() {

        log.info("none-블럭킹 호출 시작");
        long start = System.currentTimeMillis();

        Flux<Tweet> tweetFlux = WebClient.create()
                .get()
                .uri(getSlowServiceUri())
                .retrieve()
                .bodyToFlux(Tweet.class);

        tweetFlux.subscribe(tweet -> log.info(tweet.toString()));
        log.info("none-블럭킹 호출 완료 - 걸린시간 : {}ms", System.currentTimeMillis() - start);
        return tweetFlux;
    }
```

### 결과
```
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : none-블럭킹 호출 시작
INFO 96276 --- [ctor-http-nio-2] c.example.reactive.sample.WebController  : none-블럭킹 호출 완료 - 걸린시간 : 172ms
```
