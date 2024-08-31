export function randomText(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
      counter += 1;
    }
    return result;
}

export function randomTextCyrilic(length) {
    let result = '';
    const characters = 'АБВГДЃЕЖЗSИЈКЛЉМНЊОПРСТЌУФХЦЧЏШабвгдѓежзsијклљмнњопрстќуфхцчџш';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
      counter += 1;
    }
    return result;
}

export function randomNumber(number){
  const rnd = Math.floor(Math.random() * number) + 1;
  return rnd;
}