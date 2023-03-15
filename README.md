### Build docker images

```shell
cd market-data-store
docker build -t cf-utils/market-data-store .
cd ../uploaders/tcs-uploader
docker build -t cf-utils/market-data-tcs-uploader .
cd ../csv-uploader
docker build -t cf-utils/market-data-csv-uploader .
cd ..
```