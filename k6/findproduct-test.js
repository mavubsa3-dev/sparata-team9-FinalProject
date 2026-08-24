import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s',  target: 50 },  // 일반 트래픽 수준
        { duration: '30s',  target: 100 },  // 피크타임 진입
        { duration: '30s', target: 450 },  // 피크타임 급증 (30초 만에 350명 폭발적 증가)
        { duration: '3m',  target: 400 },  // 피크타임 최대 부하 3분 유지
        { duration: '30s', target: 0   },  // 종료
    ],
    thresholds: {
        http_req_duration: ['p(95)<250'],
        http_req_failed: ['rate<1'],
    },
};

export default function () {
    const productId = Math.floor(Math.random() * 10) + 1;

    const isDaily = Math.random() < 0.5;


    const baseUrl = 'http://host.docker.internal:8080/api/products/ranking';


    const url = isDaily
        ? `${baseUrl}/${productId}`
        : `${baseUrl}/week/${productId}`;



    const res = http.get(url, {
        tags: { name: isDaily ? 'DailyRanking' : 'WeekRanking' },
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    // 6. 실제 사용자처럼 0.1 ~ 0.3초 간격 대기
    sleep(Math.random() * 0.2 + 0.1);
}
