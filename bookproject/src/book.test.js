describe('CI Force Fail Test', () => {
  test('this test always fails', () => {
    expect(true).toBe(false); // ❌ 무조건 실패
  });
});
