import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '1m', target: 100 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<200'],
        http_req_failed: ['rate<0.01'],
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
