import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 50 },    // 10초 동안 50명까지 늘리기
        { duration: '20s', target: 50 },    // 50명 유지
        { duration: '10s', target: 150 },   // 10초 동안 150명까지 늘리기
        { duration: '20s', target: 150 },   // 150명 유지
        { duration: '10s', target: 300 },   // 10초 동안 300명까지 늘리기
        { duration: '20s', target: 300 },   // 300명 유지
        { duration: '10s', target: 0 },     // 10초 동안 0명으로 줄이기
    ],
};

export default function () {
    const res = http.get('http://localhost:8080/api/categories');

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}