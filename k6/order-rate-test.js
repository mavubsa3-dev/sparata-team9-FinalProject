import http from 'k6/http';

export const options = {
    scenarios: {
        constant_order_rate: {
            executor: 'constant-arrival-rate',
            rate: 5,              // 초당 5건 주문 발생
            timeUnit: '1s',
            duration: '2m',        // 2분간 지속
            preAllocatedVUs: 20,
            maxVUs: 50,
        },
    },
};

export default function () {
    http.post('http://host.docker.internal:8080/api/products/ranking/test/publish-payment-event?count=1');
}
