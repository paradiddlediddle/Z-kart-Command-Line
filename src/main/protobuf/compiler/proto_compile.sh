# shellcheck disable=SC2164
cp protoc /usr/local/bin
cd ..
protoc --java_out=../java compiler/Zkart.proto