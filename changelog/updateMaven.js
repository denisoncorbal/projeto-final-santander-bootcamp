/*
    This update is intend to be used with commit-and-tag-version lib.

    This update is an variation of gradle update provided with lib.

    For this update to work is needed that the project version is extracted to a propertie inside pom.xml.

    Alter the constant propertieName to match the name you used on your pom.xml.

    By default this update uses as propertie name to be used on pom.xml "versionProject".

    Update your pom.xml from:

    <version>Project version here (usually started with 0.0.1-SNAPSHOT)</version>

    To:

    <properties>
        <YOUR.PROPERTIE.NAME>Project version here</YOUR.PROPERTIE.NAME>
    </properties>

    <version>${YOUR.PROPERTIE.NAME}></version>
*/

const propertieName = "versionProject";
const versionRegex = new RegExp(`(?<=^[\\t\\s]{0,}<${propertieName}>)[\\w.-]{0,}(?=<\/${propertieName}>$)`,"m");

module.exports.readVersion = function(contents) {  
    const matches = versionRegex.exec(contents);
    if (matches === null) {
      throw new Error('Failed to read the version property in your pom.xml file - is it present?');
    }
  
    return matches[0];
}
  
module.exports.writeVersion = function (contents, version) {
    return contents.replace(versionRegex, () => {
      return `${version}`
    });
}