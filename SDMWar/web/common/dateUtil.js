function fixDate(dateStr) {

    let dateObj = new Date(dateStr);
    let fixedDate = dateObj.getDate() + "-" + (dateObj.getMonth()+1) + "-" + dateObj.getFullYear();

    return fixedDate;
}